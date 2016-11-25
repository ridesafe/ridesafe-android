/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ridesafe.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import io.ridesafe.android.extensions.mergeToSingleData
import io.ridesafe.android.extensions.subscribeAsyncBackground
import io.ridesafe.android.models.Data
import io.ridesafe.android.models.SensorData
import io.ridesafe.android.rest.RideSafeBackend
import io.ridesafe.android.services.ActivityObserver
import io.ridesafe.android.services.ActivityRecordService
import rx.Observable
import java.io.Serializable
import java.util.*

/**
 * Created by evoxmusic on 11/04/16.
 */
interface RideSafeBuilderImpl : Serializable {

    fun getContext(): Context?
    fun getBackend(): RideSafeBackend?
    fun getReadyCallbacks(): MutableList<(rideSafe: RideSafe) -> Unit>?
    fun getObservers(): HashSet<ActivityObserver>?
    fun getPushToBackendBatchSize(): Int
    fun getDataKey(): String?

}

class RideSafe(builder: RideSafeBuilderImpl) : RideSafeBuilderImpl by builder, Serializable {

    companion object {
        val TAG = "RideSafe"
    }

    class Builder() : RideSafeBuilderImpl {
        private var context: Context? = null
        private var backend: RideSafeBackend? = null
        private val readyCallbacks: MutableList<(rideSafe: RideSafe) -> Unit> = mutableListOf()
        private val observers: HashSet<ActivityObserver> = hashSetOf()
        private var pushToBackendBatchSize: Int = 3000
        private var dataKey: String? = null

        override fun getContext(): Context? = context
        override fun getBackend(): RideSafeBackend? = backend
        override fun getReadyCallbacks(): MutableList<(rideSafe: RideSafe) -> Unit> = readyCallbacks
        override fun getObservers(): HashSet<ActivityObserver> = observers
        override fun getPushToBackendBatchSize(): Int = pushToBackendBatchSize
        override fun getDataKey(): String? = dataKey

        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setBackend(backend: RideSafeBackend?): Builder {
            this.backend = backend
            return this
        }

        fun addReadyCallback(callback: ((rideSafe: RideSafe) -> Unit)?): Builder {
            callback?.let { readyCallbacks.add(it) }
            return this
        }

        fun addActivityObserver(observer: ActivityObserver?): Builder {
            observer?.let { observers.add(it) }
            return this
        }

        fun setPushToBackendBatchSize(batchSize: Int): Builder {
            pushToBackendBatchSize = batchSize
            return this
        }

        fun setKey(key: String): Builder {
            dataKey = key
            return this
        }

        fun build(): RideSafe {
            return RideSafe(this)
        }
    }

    private val activityRecordServiceIntent: Intent? by lazy {
        getContext()?.let { Intent(it.applicationContext, ActivityRecordService::class.java) }
    }

    private var activityRecordService: ActivityRecordService? = null
    private var isActivityRecordServiceBound: Boolean = false

    private val serviceConnectionActivityRecordService = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            startActivityRecordService()
            activityRecordService = (service as ActivityRecordService.LocalBinder).service

            isActivityRecordServiceBound = true

            ready()
        }

        override fun onServiceDisconnected(className: ComponentName) {
            isActivityRecordServiceBound = false
        }
    }

    // TODO replace it by CircularBuffer
    private val mArraySensorDatas: MutableList<SensorData> = mutableListOf()
    private val pushDatasToBackendObserver = object : ActivityObserver {
        override fun onSensorData(sensorData: SensorData) {
            mArraySensorDatas.add(sensorData)

            if (mArraySensorDatas.size % getPushToBackendBatchSize() == 0) {
                pushDatasToBackend(mArraySensorDatas.toList())
                mArraySensorDatas.clear()
            }
        }
    }

    private fun pushDatasToBackend(sensorDatas: List<SensorData>?) {
        Log.d(TAG, "push ${sensorDatas?.size ?: 0} datas to backend")

        Observable.create<List<Data>> { subscriber ->

            val datas = sensorDatas
                    ?.groupBy { it.timestamp }
                    ?.map { it.value.mergeToSingleData() }

            datas?.forEach { it.key = getDataKey() }

            subscriber.onNext(datas)
            subscriber.onCompleted()

        }.flatMap {
            getBackend()?.data?.post(it)
        }.subscribeAsyncBackground({ throwable ->
            throwable.printStackTrace()
            sensorDatas?.let { mArraySensorDatas.addAll(it) }
        }) { result ->

        }

    }

    private fun doBindService() {
        if (getContext() == null) {
            // context must not be null to start recording activity
            return
        }

        getContext()?.applicationContext?.bindService(activityRecordServiceIntent,
                serviceConnectionActivityRecordService, Context.BIND_AUTO_CREATE)
    }

    private fun doUnbindService() {
        if (getContext() == null) {
            // context must not be null to start recording activity
            return
        }

        if (isActivityRecordServiceBound) {
            getContext()?.let {
                // Detach our existing connection.
                it.applicationContext.unbindService(serviceConnectionActivityRecordService)
                isActivityRecordServiceBound = false
            }
        }
    }

    /**
     * ready method is called when the RideSafeObject is ready to by used
     */
    private fun ready() {
        getObservers()?.add(pushDatasToBackendObserver)

        // add observers
        getObservers()?.forEach { activityRecordService?.addObserver(it) }

        getReadyCallbacks()?.forEach { it(this) }
    }

    private fun startActivityRecordService() {
        // start ActivityRecordService
        getContext()?.applicationContext?.startService(activityRecordServiceIntent)
    }

    private fun stopActivityRecordService() {
        // stop ActivityRecordService
        getContext()?.applicationContext?.stopService(activityRecordServiceIntent)
    }


    init {
        init()
    }

    fun init() {
        doBindService()
    }

    fun close() {
        // remove observers
        getObservers()?.forEach { activityRecordService?.removeObserver(it) }

        stopRecordingActivity()

        doUnbindService()

        stopActivityRecordService()
    }

    fun startRecordingActivity() {
        // start data listening
        activityRecordService?.startSensor()
    }

    fun stopRecordingActivity() {
        // stop data listening
        activityRecordService?.stopSensor()

        if (mArraySensorDatas.size > 0) {
            // flush datas
            pushDatasToBackend(mArraySensorDatas)
            mArraySensorDatas.clear()
        }
    }

}
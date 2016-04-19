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

package io.ridesafe.android.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import io.ridesafe.android.extensions.getAcceleration
import java.util.*

/**
 * Created by evoxmusic on 10/04/16.
 *
 * This service take care of getting gyroscope, accelerometer data,
 * storing them locally.
 */
class ActivityRecordService : Service(), SensorEventListener, ActivityObservable {

    private val observers = HashSet<ActivityObserver>()
    private val mBinder = LocalBinder()

    private var sm: SensorManager? = null
    private var accelerometer: Sensor? = null

    inner class LocalBinder : Binder() {
        val service: ActivityRecordService
            get() = this@ActivityRecordService
    }

    override fun onCreate() {
        super.onCreate()

        // init accelerometer sensor
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sm?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // do nothing
    }

    override fun onSensorChanged(event: SensorEvent?) {
        notifyObservers(event?.getAcceleration())
    }

    override fun getObservers(): HashSet<ActivityObserver> {
        return observers
    }

    fun startSensor() = sm?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

    fun stopSensor() = sm?.unregisterListener(this)

}
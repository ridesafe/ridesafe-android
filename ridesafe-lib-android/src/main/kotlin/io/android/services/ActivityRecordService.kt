package io.android.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.IBinder
import io.android.extensions.getAcceleration
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
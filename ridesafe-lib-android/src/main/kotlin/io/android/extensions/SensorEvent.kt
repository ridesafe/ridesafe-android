package io.android.extensions

import android.hardware.SensorEvent
import io.android.models.Acceleration
import java.util.*

/**
 * Created by evoxmusic on 11/04/16.
 */
fun SensorEvent.getAcceleration() = Acceleration(Date().getLocalDate().time, values[0], values[1], values[2])
package io.android.services

import io.android.models.Acceleration

/**
 * Created by evoxmusic on 11/04/16.
 */
interface ActivityObserver {

    fun onAcceleration(acceleration: Acceleration)

}
package io.android.services

import io.android.models.Acceleration
import java.util.*

/**
 * Created by evoxmusic on 11/04/16.
 */
interface ActivityObservable {

    fun getObservers(): HashSet<ActivityObserver>

    fun addObserver(activityObserver: ActivityObserver) = getObservers().add(activityObserver)

    fun removeObserver(activityObserver: ActivityObserver) = getObservers().remove(activityObserver)

    fun notifyObservers(acceleration: Acceleration?) = getObservers().forEach { e -> acceleration?.let { e.onAcceleration(it) } }

}
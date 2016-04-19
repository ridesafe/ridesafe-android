package io.android.extensions

import io.android.models.Acceleration

/**
 * Created by evoxmusic on 12/04/16.
 */
fun List<Array<Number>>?.toAccelerations() = this?.map { Acceleration.factory(it) }
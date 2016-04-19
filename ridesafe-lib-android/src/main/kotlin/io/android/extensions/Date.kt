package io.android.extensions

import java.util.*

/**
 * Created by evoxmusic on 18/04/16.
 */
fun Date.getLocalDate(): Date {
    // add offset
    val offset = TimeZone.getDefault().getOffset(Date().time)
    return Date(this.time + offset)
}
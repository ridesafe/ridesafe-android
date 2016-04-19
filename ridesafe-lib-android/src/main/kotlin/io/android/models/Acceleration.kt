package io.android.models

/**
 * Created by evoxmusic on 10/04/16.
 *
 * Acceleration data x2 + y2 + z2
 */
data class Acceleration(val timestamp: Long,
                        val x: Float,
                        val y: Float,
                        val z: Float) {

    fun toArray() = arrayOf<Number>(timestamp, x, y, z)

    companion object {
        fun factory(array: Array<Number>) = Acceleration(
                timestamp = array[0].toLong(),
                x = array[1].toFloat(),
                y = array[2].toFloat(),
                z = array[3].toFloat()
        )
    }

}
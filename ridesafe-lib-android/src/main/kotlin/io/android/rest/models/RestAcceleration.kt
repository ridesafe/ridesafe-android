package io.android.rest.models

import io.android.models.Acceleration
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by evoxmusic on 10/04/16.
 */
interface RestAcceleration {

    @POST("acceleration")
    fun post(@Body acceleration: Acceleration): Observable<Acceleration>

    @POST("accelerations")
    fun post(@Body accelerations: List<Acceleration>): Observable<Boolean>

}
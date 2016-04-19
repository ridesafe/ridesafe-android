package io.android.rest.models

import io.android.backend.models.AccelerationForm
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 * Created by evoxmusic on 17/04/16.
 */
interface RestAccelerationForm {

    @POST("acceleration/form")
    fun post(@Body accelerationForm: AccelerationForm): Observable<Any>

}
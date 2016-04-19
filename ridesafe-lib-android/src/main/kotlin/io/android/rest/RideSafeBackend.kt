package io.android.rest

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.android.rest.models.RestAcceleration
import io.android.rest.models.RestAccelerationForm
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by evoxmusic on 10/04/16.
 */
class RideSafeBackend constructor(val host: String,
                                  val authenticationToken: String? = null,
                                  val timeout: Long = 60 * 1000L) {

    var deviceId: String? = null

    private var ra: Retrofit? = null

    val acceleration: RestAcceleration by lazy { ra?.create(RestAcceleration::class.java)!! }
    val accelerationForm: RestAccelerationForm by lazy { ra?.create(RestAccelerationForm::class.java)!! }

    init {
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor { chain ->

                    val builder = chain.request().newBuilder().addHeader("Device", "android")

                    authenticationToken?.let { builder.addHeader("Authorization", it) }

                    chain.proceed(builder.build())

                }.build()

        ra = Retrofit.Builder()
                .baseUrl(HttpUrl.parse("$host/api/v1/"))
                .client(okHttpClient)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build()


    }

}
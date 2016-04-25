/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ridesafe.android.rest

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.ridesafe.android.rest.models.RestAcceleration
import io.ridesafe.android.rest.models.RestAccelerationForm
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

                    deviceId?.let { builder.addHeader("Device-Id", it) }
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
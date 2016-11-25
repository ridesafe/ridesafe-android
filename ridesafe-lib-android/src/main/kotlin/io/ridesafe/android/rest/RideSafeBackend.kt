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

import android.content.Context
import android.provider.Settings
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jaredrummler.android.device.DeviceName
import io.ridesafe.android.rest.models.RestData
import io.ridesafe.android.rest.models.RestDataForm
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * Created by evoxmusic on 10/04/16.
 */
class RideSafeBackend constructor(val context: Context,
                                  val host: String,
                                  val authenticationToken: String? = null,
                                  val timeout: Long = 60 * 1000L) : Serializable {

    private var ra: Retrofit? = null

    val data: RestData by lazy { ra?.create(RestData::class.java)!! }
    val dataForm: RestDataForm by lazy { ra?.create(RestDataForm::class.java)!! }

    init {
        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

        val rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS

        // get device id
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        val okHttpClient = OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor { chain ->

                    val builder = chain.request().newBuilder().addHeader("Device", "android")

                    builder.addHeader("Device-Id", deviceId)
                    authenticationToken?.let { builder.addHeader("Authorization", it) }

                    // get device information
                    DeviceName.with(context).request { deviceInfo, exception ->
                        if (exception == null) {
                            builder.addHeader("Device-Brand", deviceInfo.manufacturer)
                            builder.addHeader("Device-Model", deviceInfo.marketName)
                            builder.addHeader("Device-Raw-Model", deviceInfo.model)
                        }

                        chain.proceed(builder.build())
                    }

                }.build()

        ra = Retrofit.Builder()
                .baseUrl(HttpUrl.parse("$host/api/v1/"))
                .client(okHttpClient)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build()

    }

}
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

package io.ridesafe.android.extensions

import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by evoxmusic on 11/04/16.
 */

fun <T> Observable<T>.prepareAsync(): Observable<T> = this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .onErrorResumeNext(Observable.empty<T>())
        .doOnError { it.printStackTrace() }


fun <T> Observable<T>.subscribeAsync(cls: (onNext: T) -> Unit = {}): Subscription = this.prepareAsync().subscribe { cls(it) }

fun <T> Observable<T>.subscribeAsync(doOnError: (throwable: Throwable) -> Unit, cls: (onNext: T) -> Unit = {}): Subscription =
        this.prepareAsync().doOnError { doOnError(it) }.subscribe { cls(it) }

fun <T> Observable<T>.prepareAsyncBackground(): Observable<T> = this.subscribeOn(Schedulers.io())
        .observeOn(Schedulers.computation())
        .onErrorResumeNext(Observable.empty<T>())
        .doOnError { it.printStackTrace() }


fun <T> Observable<T>.subscribeAsyncBackground(cls: (onNext: T) -> Unit = {}): Subscription = this.prepareAsyncBackground().subscribe { cls(it) }

fun <T> Observable<T>.subscribeAsyncBackground(doOnError: (throwable: Throwable) -> Unit, cls: (onNext: T) -> Unit = {}): Subscription =
        this.prepareAsyncBackground().doOnError { doOnError(it) }.subscribe { cls(it) }

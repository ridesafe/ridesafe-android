package io.android.extensions

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

@file:Suppress("UNCHECKED_CAST")

package com.example.wahyustoryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValue(
    timeout: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    callback: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)
    callback.invoke()

    if (!latch.await(timeout, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("Live data isn't there!")
    }

    return data as T
}
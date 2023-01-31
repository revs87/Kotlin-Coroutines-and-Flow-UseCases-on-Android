package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() {
    println("main starts")
    runBlocking {
        joinAll(
            async { delayDemonstration(1, 500) },
            async { delayDemonstration(2, 300) }
        )
    }
    println("main ends")
}

suspend fun delayDemonstration(number: Int, time: Long) {
    println("Coroutine $number starts work")
    // delay(time)

    Handler(Looper.getMainLooper())
        .postDelayed({
            println("Coroutine $number has finished")
        }, time)
}
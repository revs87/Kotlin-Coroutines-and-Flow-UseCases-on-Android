package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

private fun main() {
    println("main starts")
    runBlocking {
        joinAll(
            async { coroutine(1, 500) },
            async { coroutine(2, 300) }
        )
    }
    println("main ends")
}

suspend fun coroutine(number: Int, time: Long) {
    println("Coroutine $number starts work")
    delay(time)
    println("Coroutine $number has finished")
}
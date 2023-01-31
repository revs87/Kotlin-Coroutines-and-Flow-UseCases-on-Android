package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main1() = runBlocking {
    val job = launch(start = CoroutineStart.LAZY) {
        networkRequest()
        println("result received")
    }
    delay(500)
    job.start()
    println("end of runBlocking")
}

suspend fun networkRequest(): String {
    delay(500)
    return "Result"
}

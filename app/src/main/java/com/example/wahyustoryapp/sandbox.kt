package com.example.wahyustoryapp

import kotlinx.coroutines.delay

fun main() {
    val list = listOf(1,2,3,4)
    val a = list.take(5)

    list.forEach(::println)


}

suspend fun t() : String{
    delay(300L)
    return "ok"
}
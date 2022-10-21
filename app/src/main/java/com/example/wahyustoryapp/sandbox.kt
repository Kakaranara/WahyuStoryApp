package com.example.wahyustoryapp

fun main() {
    val list = listOf<Int>(1,2,3,4,5)

    val res = list.lastOrNull{ it == 5}
    println(res)
}

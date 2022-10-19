package com.example.wahyustoryapp

fun main() {
    val counter = Counter()
    val wrapper = CounterRepository(counter)
    val c= Counter()

    wrapper.increment()
    counter.increment()
    println(counter.a)
    println(wrapper.counter.a)
    println(c.a)
}

class Counter() {
    var a = 5
    fun increment() {
        a++
    }
}

class CounterRepository(var counter: Counter) {
    fun increment() {
        counter.increment()
    }
}
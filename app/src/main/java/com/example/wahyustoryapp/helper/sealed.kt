package com.example.wahyustoryapp.helper

sealed class Async<out R> private constructor() {
    data class Success<out T>(val data: T) : Async<T>()
    data class Error(val error: String) : Async<Nothing>()
    object Loading : Async<Nothing>()
}

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}
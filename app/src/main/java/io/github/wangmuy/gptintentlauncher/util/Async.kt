package io.github.wangmuy.gptintentlauncher.util

sealed class Async<out T> {
    object Loading: Async<Nothing>()

    data class Error(val errorMessage: String): Async<Nothing>()

    data class Success<out T>(val data: T): Async<T>()
}
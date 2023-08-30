package io.github.wangmuy.gptintentlauncher.util

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

sealed class Async<out T> {
    object Loading: Async<Nothing>()

    data class Error(val errorMessage: String): Async<Nothing>()

    data class Success<out T>(val data: T): Async<T>()
}

suspend fun <T> suspendRunCatching(
    dispatcher: CoroutineDispatcher,
    block: suspend () -> T
): Result<T> = try {
    withContext(dispatcher) {
        Result.success(block())
    }
} catch (ce: CancellationException) {
    throw ce
} catch (e: Exception) {
    Result.failure(e)
}
package io.github.wangmuy.gptintentlauncher.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

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
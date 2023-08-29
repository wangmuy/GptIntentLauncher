package io.github.wangmuy.gptintentlauncher.data.source

import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.data.source.local.ChatMessageDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultChatRepository(
    private val localDataSource: ChatMessageDao,
    private val dispatcher: CoroutineDispatcher,
    val scope: CoroutineScope
): ChatRepository {
    companion object {
        private const val TAG = "DefaultChatRepository$DEBUG_TAG"
    }

    override suspend fun getMessages(): List<ChatMessage> {
        return withContext(dispatcher) {
            localDataSource.getAll().toExternal()
        }
    }

    override fun getMessageStream(): Flow<List<ChatMessage>> {
        return localDataSource.observeAll().map {messages->
            withContext(dispatcher) {
                messages.toExternal()
            }
        }
    }

    override suspend fun addMessage(message: ChatMessage) {
        localDataSource.insert(message.toLocal())
    }
}
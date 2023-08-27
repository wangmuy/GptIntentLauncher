package io.github.wangmuy.gptintentlauncher.data.source

import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

class DefaultChatRepository: ChatRepository {
    companion object {
        private const val TAG = "DefaultChatRepository$DEBUG_TAG"
    }

    override suspend fun getMessages(): List<ChatMessage> {
        TODO("Not yet implemented")
    }

    override fun getMessageStream(): Flow<List<ChatMessage>> {
        TODO("Not yet implemented")
    }

    override suspend fun addMessage(message: ChatMessage) {
        TODO("Not yet implemented")
    }
}
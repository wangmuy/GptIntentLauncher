package io.github.wangmuy.gptintentlauncher.data.source

import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow

class DefaultChatRepository: ChatRepository {

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
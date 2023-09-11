package io.github.wangmuy.gptintentlauncher.chat.source

import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getMessages(): List<ChatMessage>
    fun getMessageStream(): Flow<List<ChatMessage>>

    suspend fun addMessage(message: ChatMessage)
    suspend fun clearHistory()
}
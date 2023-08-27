package io.github.wangmuy.gptintentlauncher.service

import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage

interface ChatService {
    fun setService(apiKey: String, baseUrl: String, timeoutMillis: Long, proxy: String?)
    fun setLLMConfig(configs: Map<String, Any>)
    suspend fun sendMessage(message: ChatMessage): Result<ChatMessage>
}
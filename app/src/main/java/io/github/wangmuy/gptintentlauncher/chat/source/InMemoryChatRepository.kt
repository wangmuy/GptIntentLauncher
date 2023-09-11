package io.github.wangmuy.gptintentlauncher.chat.source

import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.concurrent.atomic.AtomicInteger

class InMemoryChatRepository: ChatRepository {
    companion object {
        private const val TAG = "InMemoryChatRepository$DEBUG_TAG"
        private val COUNT = AtomicInteger(0)
    }

    private val _messagesMSF = MutableStateFlow(ArrayList<ChatMessage>())
    private val messages = _messagesMSF.asStateFlow()

    private val observableMessages: Flow<List<ChatMessage>> = messages.map { it }

    override suspend fun getMessages(): List<ChatMessage> {
        return observableMessages.first()
    }

    override fun getMessageStream(): Flow<List<ChatMessage>> {
        return observableMessages
    }

    override suspend fun addMessage(message: ChatMessage) {
        _messagesMSF.update {oldMessages->
            val newMessages = ArrayList(oldMessages)
            newMessages.add(
                ChatMessage(
                id = COUNT.incrementAndGet(),
                role = message.role,
                content = message.content,
                timeMs = message.timeMs
            )
            )
            newMessages
        }
    }

    override suspend fun clearHistory() {
        _messagesMSF.update {_->
            val newMessages = ArrayList<ChatMessage>()
            newMessages
        }
    }
}
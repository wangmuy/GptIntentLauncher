package io.github.wangmuy.gptintentlauncher.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.data.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.service.ChatService
import io.github.wangmuy.gptintentlauncher.util.Async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatScreenViewModel(
    private val chatRepository: ChatRepository,
    private val chatService: ChatService
): ViewModel() {
    companion object {
    }

    val screenUiState: MutableStateFlow<ChatScreenUiState> = MutableStateFlow(ChatScreenUiState())

    val currentChatUiState: StateFlow<Async<List<ChatMessage>>> = chatRepository
        .getMessageStream().map { Async.Success(it) }
        .catch<Async<List<ChatMessage>>> { emit(Async.Error("error loading chat histories")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Async.Loading
        )

    fun onTextChange(inputText: String) {
        screenUiState.update {
            it.copy(inputText = inputText)
        }
    }

    fun onSendMessage() {
        viewModelScope.launch {
            val input = screenUiState.value.inputText
            screenUiState.update { it.copy(inputText = "", isSending = true) }

            val sendText = input
            val msg = ChatMessage(
                role = ChatMessage.ROLE_ME,
                content = sendText)
            chatRepository.addMessage(msg)

            val sendResult = chatService.sendMessage(msg)
            screenUiState.update {
                it.copy(isSending = false)
            }
            val replyMsg = sendResult.getOrElse {e->
                ChatMessage(
                    role = ChatMessage.ROLE_APP,
                    content = e.stackTraceToString()
                )
            }
            chatRepository.addMessage(replyMsg)
        }
    }
}

data class ChatScreenUiState(
    val inputText: String = "",
    val showTemplate: Boolean = true,
    val isSending: Boolean = false
)
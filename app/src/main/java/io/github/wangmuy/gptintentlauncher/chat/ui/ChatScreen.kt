package io.github.wangmuy.gptintentlauncher.chat.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import io.github.wangmuy.gptintentlauncher.chat.ChatScreenViewModel
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.util.Async

@Composable
fun ChatScreen(
    contentPadding: PaddingValues,
    viewModel: ChatScreenViewModel
) {
    val conversationUiState by viewModel.currentChatUiState.collectAsState()
    val screenUiState by viewModel.screenUiState.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(contentPadding)) {
        Box(Modifier.weight(1f)) {
            when (conversationUiState) {
                Async.Loading -> Unit

                is Async.Error -> {
                    val errorMsg = ChatMessage(
                        role = ChatMessage.ROLE_APP,
                        content = "failed to load chat history"
                    )
                    Conversation(messages = listOf(errorMsg))
                }

                is Async.Success -> {
                    Conversation(messages = (conversationUiState as Async.Success<List<ChatMessage>>).data)
                }
            }
        }

        EditBar(
            inputText = screenUiState.inputText,
            onTextChange = viewModel::onTextChange,
            onSend = viewModel::onSendMessage)
    }
}
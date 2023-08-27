package io.github.wangmuy.gptintentlauncher.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage

@Composable
fun Conversation(messages: List<ChatMessage>) {
    val listState = rememberLazyListState()

    if (messages.isNotEmpty()) {
        LaunchedEffect(messages.last()) {
            listState.animateScrollToItem(messages.lastIndex) //, scrollOffset = 2
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(start = 4.dp, end = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState
    ) {
        items(messages, key = { it.id }) {msg->
            MessageCard(msg)
        }
//        item {
//            Box(Modifier.height(70.dp))
//        }
    }
}
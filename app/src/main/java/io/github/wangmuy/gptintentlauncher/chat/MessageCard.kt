package io.github.wangmuy.gptintentlauncher.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage

@Composable
fun MessageCard(msg: ChatMessage) {
    Row {
        Column {
            Text(text = msg.role, color = MaterialTheme.colorScheme.secondary, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = msg.content, style = MaterialTheme.typography.bodySmall)
        }
    }
}
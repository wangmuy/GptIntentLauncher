package io.github.wangmuy.gptintentlauncher.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBar(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onClear: () -> Unit
) {
    val enableSend = inputText.isNotBlank()
    var showClearConfirm by remember { mutableStateOf(false) }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Confirm") },
            text = { Text("Clear all chats?") },
            confirmButton = { Button(onClick = {
                onClear()
                showClearConfirm = false }) {
                Text("Yes")
            } },
            dismissButton = { Button(onClick = { showClearConfirm = false }) {
                Text("No")
            } }
        )
    }

    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Bottom) {
        IconButton(
            modifier = Modifier.size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = Color.Red,
                disabledContentColor = Color.Gray
            ),
            onClick = { showClearConfirm = true }) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Clear Conversation"
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(top = 10.dp, end = 10.dp)
                    .heightIn(max = 200.dp),
                value = inputText,
                onValueChange = onTextChange
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column(modifier = Modifier.size(width = 48.dp, height = 48.dp)) {
            IconButton(
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Green,
                    disabledContentColor = Color.Gray
                ),
                onClick = onSend,
                enabled = enableSend) {
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Send")
            }
        }
    }
}
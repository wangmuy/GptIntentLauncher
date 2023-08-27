package io.github.wangmuy.gptintentlauncher.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBar(
    inputText: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit
) {
    val enableSend = inputText.isNotBlank()

    Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.Bottom) {
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
        Spacer(modifier = Modifier.width(8.dp))
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
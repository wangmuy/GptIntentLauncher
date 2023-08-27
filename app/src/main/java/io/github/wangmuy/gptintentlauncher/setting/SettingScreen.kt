package io.github.wangmuy.gptintentlauncher.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.wangmuy.gptintentlauncher.data.model.ChatConfig
import io.github.wangmuy.gptintentlauncher.util.Async

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    contentpadding: PaddingValues,
    viewModel: SettingScreenViewModel
) {
    val configState by viewModel.configState.collectAsState()
    val screenUiState by viewModel.screenUiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentpadding)
            .verticalScroll(state = scrollState)
    ) {
        when (configState) {
            Async.Loading -> Unit

            is Async.Error -> {
                Text(text = (configState as Async.Error).errorMessage)
            }

            is Async.Success -> {
                val data = (configState as Async.Success<ChatConfig>).data
                LaunchedEffect(data) {
                    viewModel.onConfigLoaded(data)
                }
            }
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(10.dp),
            label = { Text(text = "apiKey") },
            value = screenUiState.apiKey,
            singleLine = true,
            onValueChange = viewModel::onApiKeyChange)

        TextField(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(10.dp),
            label = { Text(text = "baseUrl") },
            value = screenUiState.baseUrl,
            singleLine = true,
            onValueChange = viewModel::onBaseUrlChange)

        TextField(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(10.dp),
            label = { Text(text = "timeoutMillis") },
            value = screenUiState.timeoutMillisStr,
            singleLine = true,
            onValueChange = viewModel::onTimeoutMillisChange)

        TextField(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(10.dp),
            label = { Text(text = "proxy") },
            value = screenUiState.proxy,
            singleLine = true,
            onValueChange = viewModel::onProxyChange)

        TextField(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(10.dp),
            label = { Text(text = "llmConfig") },
            value = screenUiState.llmConfigStr,
            singleLine = true,
            onValueChange = viewModel::onLLMConfigChange)

        Row(modifier = Modifier.padding(8.dp)) {
            Button(onClick = {
                viewModel.saveConfig(
                    screenUiState.apiKey,
                    screenUiState.baseUrl,
                    screenUiState.timeoutMillisStr,
                    screenUiState.proxy,
                    screenUiState.llmConfigStr
                )
            }) {
                Text(text = "save")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { viewModel.loadDefaultConfig() }) {
                Text(text = "load defaults")
            }
        }
        Text(text = screenUiState.hintText)
    }
}
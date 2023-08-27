package io.github.wangmuy.gptintentlauncher.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangmuy.llmchain.llm.BaseLLM
import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import io.github.wangmuy.gptintentlauncher.data.model.ChatConfig
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

class SettingScreenViewModel(
    private val chatService: ChatService,
    private val settingDataSource: SettingDataSource
): ViewModel() {
    val screenUiState: MutableStateFlow<SettingScreenUiState> = MutableStateFlow(
        SettingScreenUiState()
    )

    val configState: StateFlow<Async<ChatConfig>> = settingDataSource
        .getConfigStream().map { Async.Success(it) }
        .catch<Async<ChatConfig>> { emit(Async.Error("error loading config")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Async.Loading
        )

    fun onConfigLoaded(config: ChatConfig) {
        screenUiState.update {
            it.copy(
                apiKey = config.apiKey,
                baseUrl = config.baseUrl,
                timeoutMillisStr = config.timeoutMillis.toString(),
                proxy = config.proxy,
                llmConfigStr = SettingRepository.llmConfigToString(config.llmConfig)
            )
        }
        val proxy = config.proxy.ifEmpty { null }
        chatService.setService(config.apiKey, config.baseUrl, config.timeoutMillis, proxy)
        chatService.setLLMConfig(config.llmConfig)
    }

    private fun clearHintText() {
        screenUiState.update {
            it.copy(hintText = "")
        }
    }

    private fun parseLLMConfig(config: ChatConfig, llmConfigStr: String) {
        llmConfigStr.lineSequence().forEach {line->
            if (line.isNotEmpty()) {
                val parts = line.split("=").map { it.trim() }
                if (parts.size == 2) {
                    when (parts[0].lowercase()) {
                        "apikey" ->
                            config.apiKey = parts[1]
                        "baseurl" ->
                            config.baseUrl = parts[1]
                        "timeoutMillis" ->
                            config.timeoutMillis = parts[1].toLong()
                        "proxy" ->
                            config.proxy = parts[1]
                        "modelname", "model_name" ->
                            config.llmConfig[BaseLLM.REQ_MODEL_NAME] = parts[1]
                        "temperature" ->
                            config.llmConfig[BaseLLM.REQ_TEMPERATURE] = parts[1].toDouble()
                        "maxtokens", "max_tokens" ->
                            config.llmConfig[BaseLLM.REQ_MAX_TOKENS] = parts[1].toInt()
                        "username", "user_name" ->
                            config.llmConfig[BaseLLM.REQ_USER_NAME] = parts[1]
                        "topp", "top_p" ->
                            config.llmConfig[BaseLLM.REQ_TOP_P] = parts[1].toInt()
                        "n" ->
                            config.llmConfig[BaseLLM.REQ_N] = parts[1].toInt()
                        "frequencypenalty", "frequency_penalty" ->
                            config.llmConfig[BaseLLM.REQ_FREQUENCY_PENALTY] = parts[1].toDouble()
                        "presencepenalty", "presence_penalty" ->
                            config.llmConfig[BaseLLM.REQ_PRESENCE_PENALTY] = parts[1].toDouble()
                    }
                }
            }
        }
    }

    fun saveConfig(
        apiKey: String, baseUrl: String, timeoutMillisStr: String,
        proxy: String, llmConfigStr: String) {
        clearHintText()
        viewModelScope.launch {
            val resultStr = try {
                val config = ChatConfig(
                    apiKey = apiKey,
                    baseUrl = baseUrl,
                    timeoutMillis = timeoutMillisStr.toLong(),
                    proxy = proxy
                )
                parseLLMConfig(config, llmConfigStr)
                settingDataSource.saveConfig(config)
                "save done"
            } catch (e: Exception) {
                "save failed: ${e.stackTraceToString()}"
            }
            screenUiState.update {
                it.copy(hintText = resultStr)
            }
        }
    }

    fun loadDefaultConfig() {
        clearHintText()
        onConfigLoaded(ChatConfig())
    }

    fun onApiKeyChange(newValue: String) {
        screenUiState.update {
            it.copy(apiKey = newValue)
        }
    }

    fun onBaseUrlChange(newValue: String) {
        screenUiState.update {
            it.copy(baseUrl = newValue)
        }
    }

    fun onTimeoutMillisChange(newValue: String) {
        screenUiState.update {
            it.copy(timeoutMillisStr = newValue)
        }
    }

    fun onProxyChange(newValue: String) {
        screenUiState.update {
            it.copy(proxy = newValue)
        }
    }

    fun onLLMConfigChange(newValue: String) {
        screenUiState.update {
            it.copy(llmConfigStr = newValue)
        }
    }
}

data class SettingScreenUiState(
    val apiKey: String = "",
    val baseUrl: String = "",
    val timeoutMillisStr: String = "",
    val proxy: String = "",
    val llmConfigStr: String = "",
    val hintText: String = ""
)
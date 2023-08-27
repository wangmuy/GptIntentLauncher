package io.github.wangmuy.gptintentlauncher.setting

import com.wangmuy.llmchain.llm.BaseLLM
import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import io.github.wangmuy.gptintentlauncher.data.model.ChatConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class SettingRepository: SettingDataSource {
    companion object {
        private const val TAG = "SettingRepository"

        private const val KEY_APIKEY = "apiKey"
        private const val KEY_BASEURL = "baseUrl"
        private const val KEY_TIMEOUT_MILLIS = "timeoutMillis"
        private const val KEY_PROXY = "proxy"
        private const val KEY_LLMCONFIG = "llmConfig"

        fun llmConfigToString(llmConfig: Map<String, Any>): String {
            return llmConfig.map { "${it.key}=${it.value}" }.joinToString("\n")
        }

        fun llmConfigFromString(str: String): Map<String, Any> {
            return str.split("\n").filter { it.isNotEmpty() }
                .associate { line ->
                    val (k, vStr) = line.split("=")
                    val v = when (k) {
                        BaseLLM.REQ_MAX_TOKENS,
                        BaseLLM.REQ_TOP_P,
                        BaseLLM.REQ_N -> vStr.toInt()

                        KEY_TIMEOUT_MILLIS,
                        BaseLLM.REQ_FREQUENCY_PENALTY,
                        BaseLLM.REQ_PRESENCE_PENALTY -> vStr.toLong()

                        BaseLLM.REQ_TEMPERATURE -> vStr.toDouble()

                        else -> vStr
                    }
                    k to v
                }
        }
    }

    private val _configMSF = MutableStateFlow(getPersistedConfig())
    private val config = _configMSF.asStateFlow()
    private val observableConfig: Flow<ChatConfig> = config.map { it }

    private fun getPersistedConfig(): ChatConfig {
        val cfg = ChatConfig()
        return cfg
    }

    override suspend fun getConfig(): ChatConfig {
        return observableConfig.first()
    }

    override fun getConfigStream(): Flow<ChatConfig> {
        return observableConfig
    }

    override suspend fun saveConfig(config: ChatConfig) {
        _configMSF.update {
            val newConfig = it.copy()
            if (config.apiKey.isNotEmpty()) {
                val apiKey = config.apiKey
                newConfig.apiKey = apiKey
            }
            if (config.baseUrl.isNotEmpty()) {
                val baseUrl = config.baseUrl
                newConfig.baseUrl = baseUrl
            }
            if (config.proxy.isNotEmpty()) {
                val proxy = config.proxy
                newConfig.proxy = proxy
            }
            if (config.llmConfig.isNotEmpty()) {
                val llmConfig = config.llmConfig
                newConfig.llmConfig = llmConfig
            }
            newConfig
        }
    }
}
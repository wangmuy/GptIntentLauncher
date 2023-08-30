package io.github.wangmuy.gptintentlauncher.setting.model

import com.wangmuy.llmchain.llm.BaseLLM

data class ChatConfig(
    var apiKey: String = "",
    var baseUrl: String = "https://api.openai.com/v1/",
    var timeoutMillis: Long = 10000,
    var proxy: String = "", // http://127.0.0.1:1091 http://192.168.3.129:1091
    var llmConfig: MutableMap<String, Any> = mutableMapOf(
        BaseLLM.REQ_TEMPERATURE to 0.8,
        BaseLLM.REQ_MODEL_NAME to "gpt-3.5-turbo",
        BaseLLM.REQ_MAX_TOKENS to 1000
    )
)
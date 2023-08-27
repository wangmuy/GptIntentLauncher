package io.github.wangmuy.gptintentlauncher.service

import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import io.github.wangmuy.gptintentlauncher.data.model.ChatConfig
import io.github.wangmuy.gptintentlauncher.data.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.data.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.SocketAddress

class LangChainService(
    val config: ChatConfig = ChatConfig(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): ChatService {
    companion object {
        fun getProxy(proxyStr: String?): Proxy? {
            if (proxyStr == null) {
                return null
            }
            if (proxyStr.startsWith("http")) {
                val splits = proxyStr.substring("http".length).split(":")
                val host = splits[0]
                val port = if (splits.size == 1) 80 else splits[1].toInt()
                return Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
            } else {
                val splits = (if (proxyStr.startsWith("socks5"))
                    proxyStr.substring("socks5".length)
                else
                    proxyStr.substring("socks".length)).split(":")
                val host = splits[0]
                val port = if (splits.size == 1) 80 else splits[1].toInt()
                return Proxy(Proxy.Type.SOCKS, InetSocketAddress(host, port))
            }
        }
    }

    private var llm: OpenAIChat? = null
    private var currentConfig: ChatConfig? = null

    override fun setService(apiKey: String, baseUrl: String, timeoutMillis: Long, proxy: String?) {
        config.apiKey = apiKey
        config.baseUrl =baseUrl
        config.timeoutMillis = timeoutMillis
        config.proxy = proxy ?: ""
    }

    override fun setLLMConfig(configs: Map<String, Any>) {
        config.llmConfig.clear()
        config.llmConfig.putAll(configs)
    }

    private fun getChatService(): OpenAIChat {
        if (currentConfig != config) {
            val cfg = config.copy().also {
                currentConfig = it
            }
            val proxy = cfg.proxy.ifEmpty { null }
            llm = OpenAIChat(
                apiKey = cfg.apiKey,
                baseUrl = cfg.baseUrl,
                timeoutMillis = cfg.timeoutMillis,
                proxy = getProxy(proxy),
                invocationParams = cfg.llmConfig
            )
        }
        return llm!!
    }

    override suspend fun sendMessage(
        message: ChatMessage
    ): Result<ChatMessage> = suspendRunCatching(dispatcher) {
        val reply = getChatService().invoke(message.content, emptyList())
        ChatMessage(
            role = ChatMessage.ROLE_BOT,
            content = reply)
    }
}
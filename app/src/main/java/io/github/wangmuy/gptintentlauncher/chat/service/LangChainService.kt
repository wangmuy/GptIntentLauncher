package io.github.wangmuy.gptintentlauncher.chat.service

import android.util.Log
import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.allapps.source.AppsRepository
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.chat.service.tools.PackageTool
import io.github.wangmuy.gptintentlauncher.chat.service.tools.ReplyTool
import io.github.wangmuy.gptintentlauncher.setting.model.ChatConfig
import io.github.wangmuy.gptintentlauncher.util.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.net.InetSocketAddress
import java.net.Proxy

class LangChainService(
    val appsRepository: AppsRepository,
    val config: ChatConfig = ChatConfig(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): ChatService {
    companion object {
        private const val TAG = "LangChainService$DEBUG_TAG"

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
    private var agentExecutor: LauncherAgentExecutor? = null
    private val tools: MutableList<BaseTool> = mutableListOf()

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

    private suspend fun getAgentExecutor(): LauncherAgentExecutor {
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
            agentExecutor = LauncherAgentExecutor(llm!!, tools)
        }
        tools.clear()
        tools.addAll(appsRepository.getApps().values.map { PackageTool(it) })
        tools.add(ReplyTool())
        Log.d(TAG, "tools.size=${tools.size}")
        agentExecutor?.also {
            // TODO toolRunArgs add AppsRepository, so we can actually start activity
        }
        return agentExecutor!!
    }

    override suspend fun sendMessage(
        message: ChatMessage
    ): Result<ChatMessage> = suspendRunCatching(dispatcher) {
        val reply = getAgentExecutor().run(message.content)
        ChatMessage(
            role = ChatMessage.ROLE_BOT,
            content = reply)
    }
}
package io.github.wangmuy.gptintentlauncher.chat.service

import android.util.Log
import com.wangmuy.llmchain.callback.CallbackManager
import com.wangmuy.llmchain.callback.DefaultCallbackHandler
import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.allapps.source.AppsRepository
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.chat.service.tools.PackageTool
import io.github.wangmuy.gptintentlauncher.chat.service.tools.ReplyTool
import io.github.wangmuy.gptintentlauncher.chat.service.tools.SearchTool
import io.github.wangmuy.gptintentlauncher.chat.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.setting.model.ChatConfig
import io.github.wangmuy.gptintentlauncher.setting.source.SettingDataSource
import io.github.wangmuy.gptintentlauncher.util.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Proxy

class LangChainService(
    private val appsRepository: AppsRepository,
    private val chatRepository: ChatRepository,
    private val settingDataSource: SettingDataSource,
    private val scope: CoroutineScope,
    private var config: ChatConfig = ChatConfig(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): ChatService {
    companion object {
        private const val TAG = "LangChainService$DEBUG_TAG"

        const val KEY_APPS_REPOSITORY = "appsRepository"
        const val KEY_CHAT_REPOSITORY = "chatRepository"
        const val KEY_COROUTINE_SCOPE = "coroutineScope"

        fun getProxy(proxyStr: String?): Proxy? {
            if (proxyStr == null) {
                return null
            }
            if (proxyStr.startsWith("http")) {
                val splits = proxyStr.substring("http".length).split(":")
                Log.d(TAG, "splits=$splits")
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

    private val callbackHandler = object: DefaultCallbackHandler() {
        override fun alwaysVerbose(): Boolean {
            return true
        }

        override fun onLLMStart(
            serialized: Map<String, Any>,
            prompts: List<String>,
            verbose: Boolean
        ) {
            agentExecutor?.agent?.also {
                it.toolRunArgs[KEY_APPS_REPOSITORY] = appsRepository
                it.toolRunArgs[KEY_CHAT_REPOSITORY] = chatRepository
                it.toolRunArgs[KEY_COROUTINE_SCOPE] = scope
                Log.d(TAG, "onLLMStart set toolRunArgs done, appsRepository=$appsRepository, chatRepository=$chatRepository, scope=$scope")
            }
        }

        override fun onText(text: String, verbose: Boolean) {
//            Log.d(TAG, "onText=$text")
            scope.launch {
                chatRepository.addMessage(ChatMessage(
                    role = ChatMessage.ROLE_SYSTEM,
                    content = text
                ))
            }
        }

        override fun onChainEnd(outputs: Map<String, Any>, verbose: Boolean) {
            scope.launch {
                val msg = StringBuilder().apply {
                    for ((k, v) in outputs) {
                        append("key=$k, value=$v\n")
                    }
                }.toString()
                chatRepository.addMessage(ChatMessage(
                    role = ChatMessage.ROLE_APP,
                    content = msg
                ))
            }
        }
    }
    private val callbackManager = CallbackManager(mutableListOf(callbackHandler))

    override fun setService(apiKey: String, baseUrl: String, timeoutMillis: Long, proxy: String?) {
        config.apiKey = apiKey
        config.baseUrl =baseUrl
        config.timeoutMillis = timeoutMillis
        config.proxy = proxy ?: ""
        Log.d(TAG, "setService tid=${Thread.currentThread().id} config=$config")
    }

    override fun setLLMConfig(configs: Map<String, Any>) {
        config.llmConfig.clear()
        config.llmConfig.putAll(configs)
    }

    private suspend fun getAgentExecutor(): LauncherAgentExecutor {
        config = settingDataSource.getConfig()
        Log.d(TAG, "getAgentExecutor config=$config")
        if (currentConfig != config) {
            val cfg = config.copy().also {
                currentConfig = it
            }
            Log.d(TAG, "getAgentExecutor tid=${Thread.currentThread().id}, config=$cfg")
            val proxy = cfg.proxy.ifEmpty { null }
            llm = OpenAIChat(
                apiKey = cfg.apiKey,
                baseUrl = cfg.baseUrl,
                timeoutMillis = cfg.timeoutMillis,
                proxy = getProxy(proxy),
                invocationParams = cfg.llmConfig
            )
            agentExecutor = LauncherAgentExecutor(llm!!, tools, callbackManager)
        }
        tools.clear()
        tools.addAll(
            appsRepository.getApps().values.map { PackageTool(it) }
        )
        tools.add(SearchTool())
        tools.add(ReplyTool())
        // agentExecutor.agent may not exist yet!
        Log.d(TAG, "tools.size=${tools.size}")
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
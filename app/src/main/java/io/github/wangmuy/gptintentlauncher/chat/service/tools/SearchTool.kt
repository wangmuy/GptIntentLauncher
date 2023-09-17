package io.github.wangmuy.gptintentlauncher.chat.service.tools

import android.util.Log
import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.chat.service.DuckDuckGoSearch
import io.github.wangmuy.gptintentlauncher.chat.service.LangChainService
import io.github.wangmuy.gptintentlauncher.chat.source.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.Proxy


class SearchTool(
    private val takeNum: Int = 10,
    proxy: Proxy? = null
): BaseTool(
    name = NAME,
    description = """
{"name": "$NAME","description": "A search engine. Useful for when you need to answer questions about current events, or you need to find more information on the internet. Remember, use this as a last resort!","parameters": {"type": "object","properties": {"query": {"type": "string","description": "The search query. You must think carefully and use all the knowledge to rewrite the user question to effective search engine phrases or sentence."}},"required": ["query"]}}
"""
) {
    companion object {
        private const val TAG = "SearchTool$DEBUG_TAG"
        const val NAME = "InternetSearchEngine"

        private val REGEX_QUERY = "\"query\"\\s*:\\s*\"(.+?)\"".toRegex()
    }

    private val ddgs = DuckDuckGoSearch(proxy = proxy)

    override fun onRun(toolInput: String, args: Map<String, Any>?): String {
        val chatRepository = args?.get(LangChainService.KEY_CHAT_REPOSITORY) as? ChatRepository
        val scope = args?.get(LangChainService.KEY_COROUTINE_SCOPE) as? CoroutineScope
        var output = "no result returned."
        try {
            var query = try {
                val root = JSONObject(toolInput)
                val params = root.optJSONObject("params")
                params?.optString("query")
                    ?.ifEmpty { root.optString("query") }
                    ?: root.optString("query") ?: ""
            } catch (e: Exception) {
                ""
            }
            if (query.isEmpty()) {
                query = REGEX_QUERY.find(toolInput)?.groupValues?.get(1)?.trim() ?: ""
            }
            if (query.isEmpty()) {
                throw IllegalArgumentException("input format error")
            }
            val results = ddgs.textLite(query).take(takeNum).toList()
            output = JSONArray().apply {
                for (result in results) {
                    val title = result["title"]
                    val body = result["body"]
                    if (title != null && body != null) {
                        val r = JSONObject()
                        r.put("title", title)
                        r.put("content", body)
                        put(r)
                    }
                }
            }.toString()
        } catch (e: Exception) {
            Log.e(TAG, "onRun failed", e)
            output += "error: $e"
            scope?.launch {
                chatRepository?.addMessage(
                    ChatMessage(
                    role = ChatMessage.ROLE_APP,
                    content = "$e\n${e.stackTraceToString()}")
                )
            }
        }
        return output
    }
}
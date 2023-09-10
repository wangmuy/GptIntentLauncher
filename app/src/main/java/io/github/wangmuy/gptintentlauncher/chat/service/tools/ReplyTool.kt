package io.github.wangmuy.gptintentlauncher.chat.service.tools

import android.util.Log
import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.chat.service.LangChainService
import io.github.wangmuy.gptintentlauncher.chat.source.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class ReplyTool: BaseTool(
    name = "reply",
    description = """
{"name": "$NAME","description": "Reply to the user, only after you have thought carefully and checked all the listing chat histories","parameters": {"type": "object","properties": {"reply": {"type": "string","description": "The content of the reply"}},"required" : ["reply"]}}
"""
) {
    companion object {
        private const val TAG = "ReplyTool$DEBUG_TAG"
        const val NAME = "reply"
    }

    override fun onRun(toolInput: String, args: Map<String, Any>?): String {
        val chatRepository = args?.get(LangChainService.KEY_CHAT_REPOSITORY) as? ChatRepository
        val scope = args?.get(LangChainService.KEY_COROUTINE_SCOPE) as? CoroutineScope
        var output = toolInput
        try {
            val root = JSONObject(toolInput)
            val params = root.optJSONObject("params")
            output = params?.optString("reply")
                ?.ifEmpty { root.optString("reply") }
                ?: root.optString("reply")
            if (output.isEmpty()) {
                throw IllegalArgumentException("input format error")
            }
            if (chatRepository != null && scope != null) {
                scope.launch {
                    chatRepository.addMessage(ChatMessage(
                        role = ChatMessage.ROLE_BOT,
                        content = output
                    ))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "toolInput=$toolInput", e)
            output = toolInput.split('\n').last()
        }
        return "You replied: $output"
    }
}
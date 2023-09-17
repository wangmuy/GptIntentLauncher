package io.github.wangmuy.gptintentlauncher.chat.service.tools

import android.util.Log
import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import org.json.JSONObject

class ReplyTool: BaseTool(
    name = "reply",
    description = """
{"name": "$NAME","description": "Reply to the user, only after you have thought carefully and checked all the listing chat histories","parameters": {"type": "object","properties": {"reply": {"type": "string","description": "The content of the reply"}},"required" : ["reply"]}}
""",
    returnDirect = true
) {
    companion object {
        private const val TAG = "ReplyTool$DEBUG_TAG"
        const val NAME = "reply"

        private val REGEX_REPLY = "\"reply\"\\s*:\\s*\"(.+?)\"".toRegex()
    }

    override fun onRun(toolInput: String, args: Map<String, Any>?): String {
        var output = toolInput
        try {
            output = try {
                val root = JSONObject(toolInput)
                val params = root.optJSONObject("params")
                params?.optString("reply")
                    ?.ifEmpty { root.optString("reply") }
                    ?: root.optString("reply")
            } catch (e: Exception) {
                ""
            }
            if (output.isEmpty()) {
                output = REGEX_REPLY.find(toolInput)?.groupValues?.get(1)?.trim() ?: ""
            }
            if (output.isEmpty()) {
                throw IllegalArgumentException("input format error")
            }
        } catch (e: Exception) {
            Log.e(TAG, "toolInput=$toolInput", e)
            output = toolInput.split('\n').last()
        }
        return output
    }
}
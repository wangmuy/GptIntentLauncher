package io.github.wangmuy.gptintentlauncher.chat.service.tools

import com.wangmuy.llmchain.tool.BaseTool

class ReplyTool: BaseTool(
    name = "reply",
    description = """{
  "name": "$NAME",
  "description": "Reply to the user",
  "parameters": {
    "type": "object",
    "properties": {
      "reply": {
        "type": "string",
        "description": "The content of the reply"
      }
    },
    "required" : ["reply"]
  }
}"""
) {
    companion object {
        const val NAME = "reply"
    }

    override fun onRun(toolInput: String, args: Map<String, Any>?): String {
        return toolInput
    }
}
package io.github.wangmuy.gptintentlauncher.chat.service.tools

import com.wangmuy.llmchain.tool.BaseTool


class SearchTool: BaseTool(
    name = NAME,
    description = """{
  "name": "$NAME",
  "description": "A search engine. Useful for when you need to answer questions about current events.",
  "parameters": {
    "type": "object",
    "properties": {
      "query": {
        "type": "string",
        "description": "the search query"
      }
    },
    "required": ["query"]
  }
}"""
) {
    companion object {
        const val NAME = "search"
    }

    override fun onRun(toolInput: String, args: Map<String, Any>?): String {
        TODO("Not yet implemented")
    }
}
package io.github.wangmuy.gptintentlauncher

import com.wangmuy.llmchain.llm.BaseLLM
import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import org.junit.Test

class RawPromptTest {
    @Test
    fun openAITest() {
        val llm = OpenAIChat(
            apiKey = LauncherAgentTest.APIKEY,
            proxy = LauncherAgentTest.PROXY,
            invocationParams = mutableMapOf(
                BaseLLM.REQ_MAX_TOKENS to 3000
            )
        )
        val promptStr = """
You are a smart android launcher assistant. Use the available tools to answer the user questions as best as you can. Only if you are not sure which available app to use, or if you find any missing required field information, you can then finish the final answer with asking the user to clarify. After the tool execution, you can observe the tool output and reply accordingly.

You have the following tools to use:

{"name":"com.google.android.apps.maps","type":"app","description":"","activities":[{"label":"Google Maps"}],"shortcuts":[],"intents":[{"name":"locateTo","description":"Open and location to specific location","parameters":{"type":"object","properties":{"action":{"type":"string","description":"The intent action string","enum":["android.intent.action.VIEW"]},"category":{"type":"string","description":"The intent category string","enum":["android.intent.category.DEFAULT"]},"data":{"type":"object","properties":{"scheme":{"type":"string","description":"The data scheme string","enum":["geo"]},"host":{"type":"string","description":"The data host string","enum":["0,0"]},"query":{"type":"string","description":"<the user mentioned location address>"}}}}}}]}

Use the following format:

Question: the input question you must answer
Thought: you should always think about what to do
ToolUse: must be one of the above tools, and MUST strictly JSON quoted between markdown json triple quotes
If the tool type is "app", ToolUse !!MUST!! conform to the following format:
```json
{"name": "<tool name>","params": {"subTool": "<one of activity/shortcut/intent, nothing else>","value": "<if type is activity, this is the corresponding label value. if type is shortcut, this is the corresponding shortcut id. if type is intent, this is the corresponding key and values according to the subTool schema>"}}
```
Otherwise, conform to the corresponding tool schema specs. for example:
```json
{"name": "<tool name>","params": {"<parameter keys and values according to the tool schema spec>"}}
```
Observation: the result of the action
... (this Thought/ToolUse/Observation can repeat N times)
Thought: I now know the final answer
Final Answer: politely reply to the user the final answer to the original input question and the observations, or continue to inquiry the user for more information.

Begin!
Question:Open google maps to Amphitheatre Parkway, Mountain View, CA
        """.trimIndent()
        val output = llm.invoke(promptStr, null)
        println("<<<<< output=$output")
    }
}
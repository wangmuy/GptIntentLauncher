package io.github.wangmuy.gptintentlauncher.chat.service

import com.wangmuy.llmchain.agent.ZeroShotAgent
import com.wangmuy.llmchain.chain.LLMChain
import com.wangmuy.llmchain.llm.BaseLLM
import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import org.json.JSONObject

class LauncherAgent(
    llmChain: LLMChain,
    allowedTools: List<String>,
    toolRunArgs: MutableMap<String, Any> = mutableMapOf()
): ZeroShotAgent(llmChain, allowedTools, toolRunArgs) {

    companion object {
        private const val TAG = "LauncherAgent$DEBUG_TAG"
        private val PREFIX = """
You are a smart android launcher assistant. Use the available tools to answer the user questions as best as you can. Only if you are not sure which available app to use, or if you find any missing required field information, you can then finish the final answer with asking the user to clarify. After the tool execution, you can observe the tool output and reply accordingly.

You have the following tools to use:
        """.trimIndent()

        private val FORMAT_INSTRUCTIONS = """
Use the following format:

Question: the input question you must answer
Thought: you should always think about what to do
ToolUse: must be one of the above tools, and MUST strictly JSON quoted between markdown json triple quotes, conforming to the corresponding tool schema specs. for example
```json
{
  "name": <tool name>,
  "params": {
    <parameter keys and values according to the tool schema spec>
  }
}
```
Observation: the result of the action
... (this Thought/ToolUse/Observation can repeat N times)
Thought: I now know the final answer
Final Answer: politely reply the final answer to the original input question and the observations, or continue to inquiry the user for more information.
        """.trimIndent()

        private val SUFFIX = """
Remember: only use the reply tool if you are out of other options, and the final answer is not concluded yet!
Begin!
Question: {input}
Thought: {agent_scratchpad}""".trimIndent()

        private val REGEX_TOOLUSE = "ToolUse:(.+?)\n".toRegex()
        private val REGEX_MD_JSON = """```json\n(.+?)\n```""".toRegex(RegexOption.DOT_MATCHES_ALL)
    }

    override fun extractToolAndInput(text: String): Pair<String, String>? {
//        println("extractToolAndInput: text=$text")
        return try {
            super.extractToolAndInput(text)
        } catch (e: IllegalStateException) {
            val extracted = REGEX_MD_JSON.find(text)?.groupValues?.get(1)
            val tool = try {
                JSONObject(extracted!!).getString("name")
            } catch(e: Exception)  {
                REGEX_TOOLUSE.find(text)?.groupValues?.get(1)?.trim() ?: finishToolName()
            }
            Pair(tool, extracted ?: text)
        }
    }

    private class PromptBuilder: ZeroShotAgent.PromptBuilder() {
        init {
            prefix = PREFIX
            formatInstructions = FORMAT_INSTRUCTIONS
            suffix = SUFFIX
        }

        override fun buildToolStrings(): String {
            return tools?.joinToString("\n\n") { it.description } ?: ""
        }
    }

    class Builder: ZeroShotAgent.Builder() {

        override fun build(): ZeroShotAgent {
            applyParams()
            val pb = PromptBuilder()
            val prompt = pb.tools(tools!!).build()
            (llm as? OpenAIChat)?.also {openAI->
                args?.get(BaseLLM.REQ_TEMPERATURE)?.also { openAI.invocationParams[BaseLLM.REQ_TEMPERATURE] = it }
                args?.get(BaseLLM.REQ_MAX_TOKENS)?.also { openAI.invocationParams[BaseLLM.REQ_MAX_TOKENS] = it }
                args?.get(BaseLLM.REQ_N)?.also { openAI.invocationParams[BaseLLM.REQ_N] = it }
            }
            val llmChain = LLMChain(prompt, llm!!, callbackManager = callbackManager)
            val toolName = tools?.map { it.name } ?: emptyList()
            return LauncherAgent(llmChain, toolName)
        }
    }
}
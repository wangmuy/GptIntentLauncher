package io.github.wangmuy.gptintentlauncher.chat.service

import com.wangmuy.llmchain.agent.AgentExecutor
import com.wangmuy.llmchain.agent.ZeroShotAgent
import com.wangmuy.llmchain.callback.BaseCallbackManager
import com.wangmuy.llmchain.llm.BaseLLM
import com.wangmuy.llmchain.schema.BaseLanguageModel
import com.wangmuy.llmchain.schema.BaseMemory
import com.wangmuy.llmchain.tool.BaseTool

// !! get a new one if llm changed
class LauncherAgentExecutor(
    val llm: BaseLanguageModel,
    val tools: MutableList<BaseTool>,
    val callbackManager: BaseCallbackManager? = null,
    val memory: BaseMemory? = null,
    val args: Map<String, Any>? = null
) {
    companion object {
        private const val MAX_ITERATION = 4

        private val DEFAULT_ARGS = mapOf<String, Any>(
            BaseLLM.REQ_TEMPERATURE to 0.0,
            BaseLLM.REQ_MAX_TOKENS to 1500,
        )
    }

    var agent: ZeroShotAgent? = null
    var agentExecutor: AgentExecutor? = null

    private fun getExecutor(): AgentExecutor {
        if (agentExecutor == null) {
            val newAgent = LauncherAgent.Builder(memory).self().llm(llm).tools(tools).also {
                if (callbackManager != null) {
                    it.callbackManager(callbackManager)
                }
                val allArgs = if (args != null) {
                    val argsMap = DEFAULT_ARGS.toMutableMap()
                    argsMap.putAll(args)
                    argsMap
                } else {
                    DEFAULT_ARGS
                }
                it.args(allArgs)
            }.build()
            agent = newAgent
            // if memory is added to AgentExecutor, memory.save() will be called twice in Chain.prepOutputs():
            // once in (AgentExecutor as Chain).invoke()
            // the other in (LLMChain as Chain).predict() -> invoke()
            agentExecutor = AgentExecutor(newAgent, tools, callbackManager/*, memory = memory*/).also {
                it.maxIterations = MAX_ITERATION
            }
        }
        return agentExecutor!!
    }

    fun run(query: String): String {
        return getExecutor().run(
            mapOf(
                "input" to query,
//                "stop" to listOf("Observation:", "observation:")
            )
        )
    }
}
package io.github.wangmuy.gptintentlauncher

import com.wangmuy.llmchain.callback.CallbackManager
import com.wangmuy.llmchain.callback.DefaultCallbackHandler
import com.wangmuy.llmchain.llm.BaseLLM
import com.wangmuy.llmchain.serviceprovider.openai.OpenAIChat
import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityDetail
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageStoreInfo
import io.github.wangmuy.gptintentlauncher.chat.service.LauncherAgentExecutor
import io.github.wangmuy.gptintentlauncher.chat.service.tools.PackageTool
import io.github.wangmuy.gptintentlauncher.chat.service.tools.ReplyTool
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.net.InetSocketAddress
import java.net.Proxy

class LauncherAgentTest {
    companion object {
        private val APIKEY = "src/test/resources/private.properties".filePathAsProperties().getProperty("APIKEY")!!
        private val PROXY: Proxy? = Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 1090))
    }

    private val callbackHandler = object: DefaultCallbackHandler() {
        override fun alwaysVerbose(): Boolean {
            return true
        }

        override fun onText(text: String, verbose: Boolean) {
            println(text)
        }

        override fun onChainEnd(outputs: Map<String, Any>, verbose: Boolean) {
            println("outputs=<<<<<")
            for ((k, v) in outputs) {
                println("key=$k, value=$v")
            }
            println(">>>>>")
        }
    }

    @Test
    fun toolUseExtractTest() {
        val output = """ToolUse: org.ppsspp.ppsspp
```json
{
  "name": "org.ppsspp.ppsspp",
  "intent": "activity",
  "value": "org.ppsspp.ppsspp/.MainActivity"
}
```
Observation:""".trimIndent()
        val regex = "ToolUse:(.+?)\n".toRegex()
        val extracted = regex.find(output)?.groupValues?.get(1)?.trim()
        println("extracted=$extracted")
        assertEquals("org.ppsspp.ppsspp", extracted)
    }

    @Test
    fun jsonExtractTest() {
        val output = """ToolUse: org.ppsspp.ppsspp
```json
{
  "name": "org.ppsspp.ppsspp",
  "intent": "activity",
  "value": "org.ppsspp.ppsspp/.MainActivity"
}
```
Observation:
        """.trimIndent()
        val regex = """```json\n(.+?)\n```""".toRegex(RegexOption.DOT_MATCHES_ALL)
        val extracted = regex.find(output)?.groupValues?.get(1)
        println("extracted=$extracted")
        assertNotNull(extracted)
    }

    @Test
    fun promptTest() {
        val llm = OpenAIChat(
            apiKey = APIKEY,
            proxy = PROXY,
            invocationParams = mutableMapOf(
                BaseLLM.REQ_MAX_TOKENS to 1500
            )
        )
        val tools = mutableListOf<BaseTool>(
            PackageTool(PackageInfo(
                pkgName = "org.ppsspp.ppsspp",
                launcherActivities = mutableListOf(
                    ActivityDetail(
                        activityInfo = null,
                        componentName = "org.ppsspp.ppsspp/.MainActivity",
                        label = "PPSSPP")
                ),
                shortcutInfos = mutableListOf(),
                storeInfo = PackageStoreInfo(
                    pkgName = "org.ppsspp.ppsspp",
                    title = "PPSSPP - PSP emulator",
                    descriptionHtml = "Play PSP games on your Android device, at high definition with extra features!<br><br>PPSSPP is the original and best PSP emulator for Android. It runs a lot of games, but depending on the power of your device all may not run at full speed. <br><br>No games are included with this download. Use your own real PSP games and turn them into .ISO or .CSO files, or simply play free homebrew games, which are available online. Put those in /PSP/GAME on your SD card / USB storage.<br><br>This is the free version. If you want to support future development, please download PPSSPP Gold instead!<br><br>See http://www.ppsspp.org for more information, and see the forums for game compatibility information.",
                    summary = "Play PSP games on your Android device, at high definition with extra features!",
                    genre = "Action"
                )
            )),
            ReplyTool(),
        )
        val callbackManager = CallbackManager(mutableListOf(callbackHandler))
        val executor = LauncherAgentExecutor(llm, tools, callbackManager)
        val output = executor.run("I want to play psp games")
        println("output=$output")
    }
}
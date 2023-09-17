package io.github.wangmuy.gptintentlauncher

import org.junit.Test

import org.junit.Assert.*

class KotlinTest {
    @Test
    fun mutableTest() {
        val mutableList: MutableList<String> = mutableListOf("1", "2")
        val list: List<String> = mutableList
        assertEquals(2, list.size)
        mutableList.add("3")
        assertEquals(3, list.size)
    }

    @Test
    fun regexTest() {
        val regex = "\"reply\"\\s*:\\s*\"(.+?)\"".toRegex()
        val toolInput = """
I should check the chat history to see if the user mentioned their name.
ToolUse: reply
{"reply": "Yes, your name is Bob."}
        """.trimIndent()
        val groupStr = regex.find(toolInput)?.groupValues?.get(1)?.trim()
        assertNotNull(groupStr)
        assertEquals("Yes, your name is Bob.", groupStr)
    }
}
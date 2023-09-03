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
}
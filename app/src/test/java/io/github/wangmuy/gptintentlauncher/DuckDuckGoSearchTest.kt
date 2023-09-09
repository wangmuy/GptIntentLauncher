package io.github.wangmuy.gptintentlauncher

import io.github.wangmuy.gptintentlauncher.chat.service.DuckDuckGoSearch
import org.junit.Test
import java.net.InetSocketAddress
import java.net.Proxy

class DuckDuckGoSearchTest {
    @Test
    fun testTextLite() {
        val ddgs = DuckDuckGoSearch(
            Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 1090)))
        val results = ddgs.textLite("dog").take(25).toList()
        for (result in results) {
            println("$result")
        }
        assert(results.size >= 20)
    }
}
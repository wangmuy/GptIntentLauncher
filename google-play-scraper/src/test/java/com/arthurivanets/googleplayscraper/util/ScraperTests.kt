package com.arthurivanets.googleplayscraper.util

import com.arthurivanets.googleplayscraper.GooglePlayRequestInterceptor
import com.arthurivanets.googleplayscraper.GooglePlayScraper
import com.arthurivanets.googleplayscraper.HumanBehaviorRequestThrottler
import com.arthurivanets.googleplayscraper.requests.GetAppDetailsParams
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.net.InetSocketAddress
import java.net.Proxy
import java.time.Duration

class ScraperTests {

    @Test
    fun `simple scraper`() {
        val config = GooglePlayScraper.Config(throttler = HumanBehaviorRequestThrottler())
        val client = OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress("127.0.0.1", 1090)))
            .addInterceptor(GooglePlayRequestInterceptor(config.throttler, config.userAgentProvider))
            .connectTimeout(Duration.ofSeconds(60))
            .readTimeout(Duration.ofSeconds(60))
            .writeTimeout(Duration.ofSeconds(60))
            .build()
        val scraper = GooglePlayScraper(config, client)
        val params = GetAppDetailsParams(appId = "org.ppsspp.ppsspp")
        val response = scraper.getAppDetails(params).execute()
        println("${response.result}")
        assertTrue(response.isSuccess)
    }
}
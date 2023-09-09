package io.github.wangmuy.gptintentlauncher.chat.service

import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException
import java.net.Proxy
import java.net.URLDecoder


// Bing chat to rewrite https://github.com/deedy5/duckduckgo_search
class DuckDuckGoSearch(
    proxy: Proxy? = null,
    private val headers: Headers = Headers.Builder()
        .add("User-Agent", USERAGENTS.random())
        .add("Referer", "https://duckduckgo.com/")
        .build(),
    private val client: OkHttpClient = OkHttpClient.Builder()
        .proxy(proxy)
        .followRedirects(true)
        .build()
) {
    companion object {
        private val USERAGENTS = listOf(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36",
        )

        val REGEX_500_IN_URL = Regex("[5-9][0-9][0-9]-[0-9][0-9]\\.js")
    }

    private fun getURL(
        method: String,
        url: String,
        data: Map<String, String>? = null
    ): Response? {
        val requestBuilder = Request.Builder().url(url)
        if (method == "POST") {
            val formBodyBuilder = FormBody.Builder()
            data?.forEach { (key, value) ->
                formBodyBuilder.add(key, value)
            }
            requestBuilder.post(formBodyBuilder.build())
        }
        for (i in 0 until 3) {
            try {
                val response = client.newCall(requestBuilder.build()).execute()
                val found500 = is500InUrl(response.request.url.toString())
                val code = response.code
                if (found500 || code == 202) {
                    throw IOException("500InUrl=$found500, code=$code")
                }
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                if (code == 200) {
                    return response
                }
            } catch (e: Exception) {
                println("_get_url() $url ${e.javaClass.name} $e")
                if (i >= 2 || e.message?.contains("418") == true) {
                    throw e
                }
            }
        }
        return null
    }

    private fun getVqd(keywords: String): String? {
        val response = getURL("POST", "https://duckduckgo.com", mapOf("q" to keywords))
        if (response != null) {
            val responseBody = response.body?.string()
            if (responseBody != null) {
                for ((c1, c2) in listOf(
                    Pair("vqd=\"", "\""),
                    Pair("vqd=", "&"),
                    Pair("vqd='", "'")
                )) {
                    try {
                        val start = responseBody.indexOf(c1) + c1.length
                        val end = responseBody.indexOf(c2, start)
                        return responseBody.substring(start, end)
                    } catch (ex: Exception) {
                        println("_get_vqd() keywords=$keywords vqd not found")
                    }
                }
            }
        }
        return null
    }

    private fun is500InUrl(url: String): Boolean {
        return url.contains(REGEX_500_IN_URL)
    }

    private fun normalize(rawHtml: String): String {
        return Jsoup.parse(rawHtml).text()
    }

    private fun normalizeUrl(url: String): String {
        return URLDecoder.decode(url, "UTF-8").replace(" ", "+")
    }

    fun textLite(
        keywords: String,
        region: String = "wt-wt",
        timeLimit: String? = null
    ): Sequence<Map<String, String?>> {
        require(keywords.isNotEmpty()) { "keywords is mandatory" }

        val cache = mutableSetOf<String>()
        val payload = mutableMapOf(
            "q" to keywords,
            "kl" to region
        )
        if (timeLimit != null) {
            payload["df"] = timeLimit
        }
        return sequence {
            for (s in listOf("0", "20", "70", "120")) {
                payload["s"] = s
                val response = getURL(
                    "POST",
                    "https://lite.duckduckgo.com/lite/",
                    payload
                ) ?: break

                val responseBody = response.body?.string()
                if (responseBody.isNullOrEmpty() || responseBody.contains("No more results.")) {
                    yield(emptyMap())
                    return@sequence
                }
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                if ("No more results." in responseBody) return@sequence

                val document = Jsoup.parse(responseBody)
                var title: String? = null
                var href: String? = null
                var body: String? = null
                document.select("table:last-of-type tr").forEachIndexed { index, element ->
                    when (index % 4) {
                        0 -> {
                            href = element.selectFirst("a")?.attr("href")
                            if (
                                href == null ||
                                href in cache ||
                                href == "http://www.google.com/search?q=$keywords"
                            ) {
                                title = null
                                href = null
                                body = null
                            } else {
                                cache.add(href!!)
                                title = element.selectFirst("a")?.text()
                            }
                        }
                        1 -> body = element.selectFirst(".result-snippet")?.text()?.trim()
                        2 -> if (title != null && body != null) {
                            yield(
                                mapOf(
                                    "title" to title?.let { normalize(it) },
                                    "href" to href?.let { normalizeUrl(it) },
                                    "body" to body?.let { normalize(it) },
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
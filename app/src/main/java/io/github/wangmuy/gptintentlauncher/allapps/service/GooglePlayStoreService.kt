package io.github.wangmuy.gptintentlauncher.allapps.service

import android.util.Log
import com.arthurivanets.googleplayscraper.GooglePlayScraper
import com.arthurivanets.googleplayscraper.HumanBehaviorRequestThrottler
import com.arthurivanets.googleplayscraper.requests.GetAppDetailsParams
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.allapps.model.AppInfo
import io.github.wangmuy.gptintentlauncher.util.suspendRunCatching
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GooglePlayStoreService(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
): AppStoreService {
    companion object {
        private const val TAG = "GooglePlayStoreService$DEBUG_TAG"
    }

    private val scraper = GooglePlayScraper(
        GooglePlayScraper.Config(
            throttler = HumanBehaviorRequestThrottler()
        )
    )

    override suspend fun getAppInfo(pkgName: String) = suspendRunCatching(dispatcher) {
        Log.d(TAG, "getAppInfo pkgName=$pkgName")
        val params = GetAppDetailsParams(appId = pkgName)
        val response = scraper.getAppDetails(params).execute()
        if (response.isSuccess) {
            val details = response.requireResult()
            AppInfo(
                pkgName,
                details.title,
                details.descriptionHtml,
                details.summary,
                details.genre,
                details.familyGenre)
        } else {
            val error = response.requireError()
            Log.e(TAG, "", error)
            throw error
        }
    }
}
package io.github.wangmuy.gptintentlauncher.allapps.source

import android.graphics.Rect
import android.os.Bundle
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityDetail
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import kotlinx.coroutines.flow.Flow

interface AppsRepository {
    suspend fun getApps(): Map<String, PackageInfo>
    fun getAppsStream(): Flow<Map<String, PackageInfo>>

    fun startActivity(activityDetail: ActivityDetail, viewBounds: Rect? = null, opts: Bundle? = null)
}
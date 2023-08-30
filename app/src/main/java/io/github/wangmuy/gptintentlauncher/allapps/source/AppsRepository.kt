package io.github.wangmuy.gptintentlauncher.allapps.source

import android.graphics.Rect
import android.os.Bundle
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityInfo
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import kotlinx.coroutines.flow.Flow

interface AppsRepository {
    suspend fun getApps(): Map<String, PackageInfo>
    fun getAppsStream(): Flow<Map<String, PackageInfo>>

    fun startActivity(activityInfo: ActivityInfo, viewBounds: Rect?, opts: Bundle?)
}
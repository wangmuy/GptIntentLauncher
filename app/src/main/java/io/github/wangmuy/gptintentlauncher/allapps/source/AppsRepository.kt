package io.github.wangmuy.gptintentlauncher.allapps.source

import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import kotlinx.coroutines.flow.Flow

interface AppsRepository {
    suspend fun getApps(): Map<String, PackageInfo>
    fun getAppsStream(): Flow<Map<String, PackageInfo>>
}
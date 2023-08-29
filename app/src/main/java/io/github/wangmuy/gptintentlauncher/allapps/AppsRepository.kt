package io.github.wangmuy.gptintentlauncher.allapps

import kotlinx.coroutines.flow.Flow

interface AppsRepository {
    suspend fun getApps(): Map<String, PackageInfo>
    fun getAppsStream(): Flow<Map<String, PackageInfo>>
}
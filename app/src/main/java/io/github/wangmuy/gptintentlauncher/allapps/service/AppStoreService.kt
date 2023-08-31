package io.github.wangmuy.gptintentlauncher.allapps.service

import io.github.wangmuy.gptintentlauncher.allapps.model.AppInfo

interface AppStoreService {
    suspend fun getAppInfo(pkgName: String): Result<AppInfo>
}
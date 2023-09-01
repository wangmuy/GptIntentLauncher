package io.github.wangmuy.gptintentlauncher.allapps.service

import io.github.wangmuy.gptintentlauncher.allapps.model.PackageStoreInfo

interface AppStoreService {
    suspend fun getAppInfo(pkgName: String): Result<PackageStoreInfo>
}
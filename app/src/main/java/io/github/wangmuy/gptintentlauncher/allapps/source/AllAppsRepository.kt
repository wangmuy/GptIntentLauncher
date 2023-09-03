package io.github.wangmuy.gptintentlauncher.allapps.source

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.LauncherApps
import android.content.pm.LauncherApps.ShortcutQuery
import android.graphics.Rect
import android.os.Bundle
import android.os.Process
import android.os.UserHandle
import android.util.DisplayMetrics
import android.util.Log
import com.arthurivanets.googleplayscraper.util.ScraperError
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityDetail
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import io.github.wangmuy.gptintentlauncher.allapps.model.ShortcutDetail
import io.github.wangmuy.gptintentlauncher.allapps.service.AppStoreService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

class AllAppsRepository(
    private val context: Context,
    private val packageStoreInfoDao: PackageStoreInfoDao,
    private val appStoreService: AppStoreService,
    private val dispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope
): AppsRepository, LauncherApps.Callback() {
    companion object {
        private const val TAG = "AllAppsRepository$DEBUG_TAG"

        private const val MAX_GET_FROM_STORE = 5
    }

    private val launcherApps: LauncherApps =
        context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

    private val _appsMSF = MutableStateFlow(HashMap<String, PackageInfo>())
    private val apps = _appsMSF.asStateFlow()
    private val observableApps: Flow<Map<String, PackageInfo>> = apps.map { it }

    init {
        launcherApps.registerCallback(this)
        scope.launch(dispatcher) {
            refreshApps()
        }
    }

    private val count = AtomicInteger(0)

    private fun makePackageInfo(pkgName: String): PackageInfo {
        val pkgInfo = PackageInfo(pkgName)
        scope.launch(dispatcher) {
            val localInfo = packageStoreInfoDao.get(pkgName)
            if (localInfo == null && count.getAndIncrement() < MAX_GET_FROM_STORE) {
                appStoreService.getAppInfo(pkgName)
                    .onSuccess {
                        Log.d(TAG, "get appInfo=$it")
                        pkgInfo.storeInfo = it
                        packageStoreInfoDao.insert(it.toLocal())
                    }
                    .onFailure {
//                        Log.e(TAG, "failed to get appInfo from store", it)
                        // if 404, save default to prevent retry
                        if (it is ScraperError.HttpError && it.statusCode == 404) {
                            Log.d(TAG, "statusCode==404, save default for $pkgName")
                            packageStoreInfoDao.insert(
                                LocalPackageStoreInfo(packageName = pkgName))
                        }
                    }
            }
            // else if somehow outdated, then update
        }
        return pkgInfo
    }

    @SuppressLint("InlinedApi")
    private suspend fun refreshApps() {
        val pkgInfos = withContext(dispatcher) {
            Log.d(TAG, "refreshApps beg")
            val packageInfoMap = HashMap<String, PackageInfo>()
            for (userHandle in launcherApps.profiles) {
                val allActivityInfos = launcherApps.getActivityList(null, userHandle)
                for (info in allActivityInfos) {
                    val pkgName = info.applicationInfo.packageName
                    val activityDetail = ActivityDetail(
                        info, info.componentName.flattenToString(), info.label.toString(), info.getIcon(DisplayMetrics.DENSITY_DEFAULT))
                    packageInfoMap.getOrPut(pkgName) { makePackageInfo(pkgName) }
                        .launcherActivities.add(activityDetail)
                }
            }
            if (launcherApps.hasShortcutHostPermission()) {
                for (userHandle in launcherApps.profiles) {
                    for (pkgName in packageInfoMap.keys) {
                        val shortcutQuery = ShortcutQuery()
                        shortcutQuery.setQueryFlags(
                            ShortcutQuery.FLAG_MATCH_DYNAMIC
                                    or ShortcutQuery.FLAG_MATCH_MANIFEST
                                    or ShortcutQuery.FLAG_MATCH_PINNED
                                    or ShortcutQuery.FLAG_MATCH_PINNED_BY_ANY_LAUNCHER
                        )
                        shortcutQuery.setPackage(pkgName)
                        val shortcuts = launcherApps.getShortcuts(shortcutQuery, userHandle)
                        if (!shortcuts.isNullOrEmpty()) {
                            Log.d(TAG, "userHandle=$userHandle for shortcuts=$shortcuts")
                            packageInfoMap[pkgName]?.shortcutInfos?.addAll(
                                shortcuts.map { ShortcutDetail(it) }
                            )
                        }
                    }
                }
            } else {
                Log.d(TAG, "hasShortcutHostPermission==false")
            }
            Log.d(TAG, "refreshApps end")
            packageInfoMap
        }
        _appsMSF.update {
            pkgInfos
        }
    }

    override suspend fun getApps(): Map<String, PackageInfo> {
        return observableApps.first()
    }

    override fun getAppsStream(): Flow<Map<String, PackageInfo>> {
        return observableApps
    }

    override fun startActivity(activityDetail: ActivityDetail, viewBounds: Rect?, opts: Bundle?) {
        launcherApps.startMainActivity(
            ComponentName.unflattenFromString(activityDetail.componentName),
            Process.myUserHandle(), viewBounds, opts)
    }

    override fun onPackageRemoved(packageName: String?, user: UserHandle?) {
        _appsMSF.update {
            it.remove(packageName)
            it
        }
    }

    private fun updatePackage(packageName: String, user: UserHandle?) {
        _appsMSF.update {map->
            val activityInfos = launcherApps.getActivityList(packageName, user)
            val activities =
                map.getOrPut(packageName) { makePackageInfo(packageName) }.launcherActivities
            activities.clear()
            activities.addAll(activityInfos.map {info->
                ActivityDetail(info, info.componentName.flattenToString(), info.label.toString(), info.getIcon(DisplayMetrics.DENSITY_DEFAULT)) })
            map
        }
    }

    override fun onPackageAdded(packageName: String?, user: UserHandle?) {
        if (packageName != null) {
            updatePackage(packageName, user)
        }
    }

    override fun onPackageChanged(packageName: String?, user: UserHandle?) {
        if (packageName != null) {
            updatePackage(packageName, user)
        }
    }

    override fun onPackagesAvailable(
        packageNames: Array<out String>?,
        user: UserHandle?,
        replacing: Boolean
    ) {
        if (packageNames != null) {
            _appsMSF.update {
                for (pkgName in packageNames) {
                    it[pkgName]?.available = true
                }
                it
            }
        }
    }

    override fun onPackagesUnavailable(
        packageNames: Array<out String>?,
        user: UserHandle?,
        replacing: Boolean
    ) {
        if (packageNames != null) {
            _appsMSF.update {
                for (pkgName in packageNames) {
                    it[pkgName]?.available = false
                }
                it
            }
        }
    }
}
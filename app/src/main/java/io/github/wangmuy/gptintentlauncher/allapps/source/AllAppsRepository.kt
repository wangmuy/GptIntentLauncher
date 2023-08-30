package io.github.wangmuy.gptintentlauncher.allapps.source

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.LauncherApps
import android.content.pm.LauncherApps.ShortcutQuery
import android.os.UserHandle
import android.util.DisplayMetrics
import android.util.Log
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityInfo
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
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

class AllAppsRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
    private val scope: CoroutineScope
): AppsRepository, LauncherApps.Callback() {
    companion object {
        private const val TAG = "AllAppsRepository$DEBUG_TAG"
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

    @SuppressLint("InlinedApi")
    private suspend fun refreshApps() {
        val pkgInfos = withContext(dispatcher) {
            Log.d(TAG, "refreshApps beg")
            val packageInfoMap = HashMap<String, PackageInfo>()
            for (userHandle in launcherApps.profiles) {
                val allApps = launcherApps.getActivityList(null, userHandle)
                for (app in allApps) {
                    val pkgName = app.applicationInfo.packageName
                    val activityInfo = ActivityInfo(
                        app, app.label.toString(), app.getIcon(DisplayMetrics.DENSITY_DEFAULT))
                    packageInfoMap.getOrPut(pkgName) { PackageInfo(pkgName) }
                        .launcherActivities.add(activityInfo)
                }
            }
            try {
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
                        if (shortcuts != null) {
                            packageInfoMap[pkgName]?.shortcutInfos?.addAll(shortcuts)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "failed to get shortcuts", e)
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

    override fun onPackageRemoved(packageName: String?, user: UserHandle?) {
        _appsMSF.update {
            it.remove(packageName)
            it
        }
    }

    private fun updatePackage(packageName: String, user: UserHandle?) {
        _appsMSF.update {map->
            val apps = launcherApps.getActivityList(packageName, user)
            val activities =
                map.getOrPut(packageName) { PackageInfo(packageName) }.launcherActivities
            activities.clear()
            activities.addAll(apps.map {app->
                ActivityInfo(app, app.label.toString(), app.getIcon(DisplayMetrics.DENSITY_DEFAULT)) })
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
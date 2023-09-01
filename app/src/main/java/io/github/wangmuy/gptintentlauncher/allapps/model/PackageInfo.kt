package io.github.wangmuy.gptintentlauncher.allapps.model

import android.content.pm.ShortcutInfo

/** from AppsRepository */
class PackageInfo(
    val pkgName: String,
    val launcherActivities: MutableList<ActivityInfo> = ArrayList(),
    val shortcutInfos: MutableList<ShortcutInfo> = ArrayList(),
    var available: Boolean = true,
    var storeInfo: PackageStoreInfo? = null
)
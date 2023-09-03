package io.github.wangmuy.gptintentlauncher.allapps.model

/** from AppsRepository */
class PackageInfo(
    val pkgName: String,
    val launcherActivities: MutableList<ActivityDetail> = ArrayList(),
    val shortcutInfos: MutableList<ShortcutDetail> = ArrayList(),
    var available: Boolean = true,
    var storeInfo: PackageStoreInfo? = null
)
package io.github.wangmuy.gptintentlauncher.allapps

import android.content.pm.ShortcutInfo

class PackageInfo(
    val pkgName: String,
    val launcherActivities: MutableList<ActivityInfo> = ArrayList(),
    val shortcutInfos: MutableList<ShortcutInfo> = ArrayList(),
    var available: Boolean = true
) {
}
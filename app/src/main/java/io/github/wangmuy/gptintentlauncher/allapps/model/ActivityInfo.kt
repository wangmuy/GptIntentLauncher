package io.github.wangmuy.gptintentlauncher.allapps.model

import android.content.pm.LauncherActivityInfo
import android.graphics.drawable.Drawable

/** from AppsRepository. one PackageInfo may contain multiple launcher ActivityInfo */
class ActivityInfo(
    val activityInfo: LauncherActivityInfo,
    val label: String,
    val icon: Drawable
) {
    override fun toString(): String {
        return "ActivityInfo(activityInfo=$activityInfo, label='$label')"
    }
}
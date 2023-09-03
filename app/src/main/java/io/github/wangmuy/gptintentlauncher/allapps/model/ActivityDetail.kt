package io.github.wangmuy.gptintentlauncher.allapps.model

import android.content.pm.LauncherActivityInfo
import android.graphics.drawable.Drawable

/** from AppsRepository. one PackageInfo may contain multiple launcher ActivityInfo */
class ActivityDetail(
    val activityInfo: LauncherActivityInfo?,
    val componentName: String = activityInfo?.componentName?.flattenToString() ?: "",
    val label: String,
    val icon: Drawable? = null
) {
    override fun toString(): String {
        return "ActivityDetail(label='$label', activityInfo=$activityInfo, componentName='$componentName')"
    }
}
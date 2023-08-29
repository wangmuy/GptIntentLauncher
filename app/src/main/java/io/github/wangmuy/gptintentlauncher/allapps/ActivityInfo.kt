package io.github.wangmuy.gptintentlauncher.allapps

import android.content.pm.LauncherActivityInfo
import android.graphics.drawable.Drawable

class ActivityInfo(
    val activityInfo: LauncherActivityInfo,
    val label: String,
    val icon: Drawable
) {
    override fun toString(): String {
        return "ActivityInfo(activityInfo=$activityInfo, label='$label')"
    }
}
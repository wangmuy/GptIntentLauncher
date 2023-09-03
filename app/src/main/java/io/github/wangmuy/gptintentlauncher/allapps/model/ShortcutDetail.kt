package io.github.wangmuy.gptintentlauncher.allapps.model

import android.content.pm.ShortcutInfo
import java.util.UUID

class ShortcutDetail(
    val shortcutInfo: ShortcutInfo?,
    val id: String = shortcutInfo?.id ?: UUID.randomUUID().toString(),
    val shortLabel: String = shortcutInfo?.shortLabel.toString() ?: "",
    val longLabel: String = shortcutInfo?.longLabel.toString() ?: "",
    val categories: List<String> = shortcutInfo?.categories?.toList() ?: emptyList()
) {
    override fun toString(): String {
        return "ShortcutDetail(shortcutInfo=$shortcutInfo, shortLabel='$shortLabel', longLabel='$longLabel', categories=$categories)"
    }
}
package io.github.wangmuy.gptintentlauncher.allapps.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityInfo

@Composable
fun LaunchCard(
    activityInfo: ActivityInfo,
    onClick: (activityInfo: ActivityInfo) -> Unit
) {
    Text(
        modifier = Modifier.fillMaxWidth().clickable { onClick(activityInfo) },
        style = MaterialTheme.typography.headlineSmall,
        text = activityInfo.label,
    )
}
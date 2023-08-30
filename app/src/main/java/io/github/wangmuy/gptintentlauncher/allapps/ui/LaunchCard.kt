package io.github.wangmuy.gptintentlauncher.allapps.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityInfo

@Composable
fun LaunchCard(
    activityInfo: ActivityInfo,
    onClick: (activityInfo: ActivityInfo, viewBounds: android.graphics.Rect?) -> Unit
) {
    var rootBounds by remember { mutableStateOf(Rect(Offset.Zero, Size.Zero)) }

    Text(
        modifier = Modifier.fillMaxWidth()
            .onGloballyPositioned {coordinates->
                rootBounds = coordinates.boundsInWindow()
            }
            .clickable {
                onClick(activityInfo,
                    android.graphics.Rect(
                        rootBounds.left.toInt(),
                        rootBounds.top.toInt(),
                        rootBounds.right.toInt(),
                        rootBounds.bottom.toInt()
                    )
                )},
        style = MaterialTheme.typography.headlineSmall,
        text = activityInfo.label,
    )
}
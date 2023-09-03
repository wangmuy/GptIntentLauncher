package io.github.wangmuy.gptintentlauncher.allapps.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityDetail

@Composable
fun LaunchCard(
    activityDetail: ActivityDetail,
    onClick: (activityInfo: ActivityDetail, viewBounds: android.graphics.Rect?) -> Unit
) {
    var rootBounds by remember { mutableStateOf(Rect(Offset.Zero, Size.Zero)) }

    Row {
        Image(
            modifier = Modifier.size(48.dp),
            painter = rememberDrawablePainter(drawable = activityDetail.icon),
            contentDescription = "appIcon"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .onGloballyPositioned { coordinates ->
                    rootBounds = coordinates.boundsInWindow()
                }
                .clickable {
                    onClick(
                        activityDetail,
                        android.graphics.Rect(
                            rootBounds.left.toInt(),
                            rootBounds.top.toInt(),
                            rootBounds.right.toInt(),
                            rootBounds.bottom.toInt()
                        )
                    )
                },
            style = MaterialTheme.typography.headlineSmall,
            text = activityDetail.label,
        )
    }
}
package io.github.wangmuy.gptintentlauncher.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreenId: Int,
    onClickChat: () -> Unit,
    onClickAllApps: () -> Unit,
    onClickSetting: () -> Unit
) {
    TopAppBar(
        title = { Text(text = "GptIntentLauncher") },
        actions = {
            IconButton(onClick = onClickChat) {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = "chat",
                    tint = getScreenButtonTint(NavigationViewModel.SCREEN_CHAT, currentScreenId)
                )
            }
            IconButton(onClick = onClickAllApps) {
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = "allApps",
                    tint = getScreenButtonTint(NavigationViewModel.SCREEN_ALLAPPS, currentScreenId)
                )
            }
            IconButton(onClick = onClickSetting) {
                Icon(
                    Icons.Rounded.Settings,
                    contentDescription = "settings",
                    tint = getScreenButtonTint(NavigationViewModel.SCREEN_SETTING, currentScreenId)
                )
            }
        }
    )
}

@Composable
fun getScreenButtonTint(buttonId: Int, currentScreenId: Int) =
    if (currentScreenId == buttonId)
        Color.Red
    else
        LocalContentColor.current
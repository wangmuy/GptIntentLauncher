package io.github.wangmuy.gptintentlauncher.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import io.github.wangmuy.gptintentlauncher.chat.ChatScreen
import io.github.wangmuy.gptintentlauncher.chat.ChatScreenViewModel
import io.github.wangmuy.gptintentlauncher.setting.SettingScreen
import io.github.wangmuy.gptintentlauncher.setting.SettingScreenViewModel
import io.github.wangmuy.gptintentlauncher.ui.theme.GptIntentLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    navigationViewModel: NavigationViewModel,
    chatViewModel: ChatScreenViewModel,
    settingViewModel: SettingScreenViewModel
) {
    val navigationState by navigationViewModel.navigationState.collectAsState()

    Scaffold(
        topBar = {
            TopBar(
                navigationState.currentScreenId,
                onClickChat = {
                    navigationViewModel.onSelectScreen(NavigationViewModel.SCREEN_CHAT)
                },
                onClickAllApps = {
                    navigationViewModel.onSelectScreen(NavigationViewModel.SCREEN_ALLAPPS)
                },
                onClickSetting = {
                    navigationViewModel.onSelectScreen(NavigationViewModel.SCREEN_SETTING)
                }
            )
        }
    ) {contentPadding->
        when (navigationState.currentScreenId) {
            NavigationViewModel.SCREEN_CHAT -> {
                ChatScreen(contentPadding, chatViewModel)
            }
            NavigationViewModel.SCREEN_ALLAPPS -> {
                ;
            }
            NavigationViewModel.SCREEN_SETTING -> {
                SettingScreen(contentPadding, settingViewModel)
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    GptIntentLauncherTheme {
//        App(NavigationViewModel(), ChatScreenViewModel(), SettingScreenViewModel())
//    }
//}
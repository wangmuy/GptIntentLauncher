package io.github.wangmuy.gptintentlauncher.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import io.github.wangmuy.gptintentlauncher.chat.ChatScreen
import io.github.wangmuy.gptintentlauncher.chat.ChatScreenViewModel
import io.github.wangmuy.gptintentlauncher.data.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.InMemoryChatRepository
import io.github.wangmuy.gptintentlauncher.service.ChatService
import io.github.wangmuy.gptintentlauncher.service.LangChainService
import io.github.wangmuy.gptintentlauncher.setting.SettingDataSource
import io.github.wangmuy.gptintentlauncher.setting.SettingRepository
import io.github.wangmuy.gptintentlauncher.setting.SettingScreen
import io.github.wangmuy.gptintentlauncher.setting.SettingScreenViewModel
import io.github.wangmuy.gptintentlauncher.ui.theme.GptIntentLauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    chatRepository: ChatRepository,
    langChainService: ChatService,
    settingDataSource: SettingDataSource
) {
    val navigationViewModel = remember { NavigationViewModel() }
    val navigationState by navigationViewModel.navigationState.collectAsState()

    val chatViewModel = remember {
        ChatScreenViewModel(chatRepository, langChainService)
    }

    val settingViewModel = remember {
        SettingScreenViewModel(langChainService, settingDataSource)
    }

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GptIntentLauncherTheme {
        App(InMemoryChatRepository(), LangChainService(), SettingRepository())
    }
}
package io.github.wangmuy.gptintentlauncher.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import io.github.wangmuy.gptintentlauncher.allapps.AllAppsScreenViewModel
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import io.github.wangmuy.gptintentlauncher.allapps.source.AppsRepository
import io.github.wangmuy.gptintentlauncher.allapps.ui.AllAppsScreen
import io.github.wangmuy.gptintentlauncher.chat.ChatScreenViewModel
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.chat.service.ChatService
import io.github.wangmuy.gptintentlauncher.chat.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.chat.ui.ChatScreen
import io.github.wangmuy.gptintentlauncher.setting.SettingScreenViewModel
import io.github.wangmuy.gptintentlauncher.setting.model.ChatConfig
import io.github.wangmuy.gptintentlauncher.setting.source.SettingDataSource
import io.github.wangmuy.gptintentlauncher.setting.ui.SettingScreen
import io.github.wangmuy.gptintentlauncher.ui.theme.GptIntentLauncherTheme
import io.github.wangmuy.gptintentlauncher.util.suspendRunCatching
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    navigationViewModel: NavigationViewModel,
    chatViewModel: ChatScreenViewModel,
    allAppsViewModel: AllAppsScreenViewModel,
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
                AllAppsScreen(contentPadding, allAppsViewModel)
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
    // todo https://insert-koin.io/docs/reference/koin-compose/multiplatform
    val emptyChatRepository = object: ChatRepository {
        override suspend fun getMessages(): List<ChatMessage> {
            return listOf(ChatMessage(role = ChatMessage.ROLE_BOT, content = "hello"))
        }

        override fun getMessageStream(): Flow<List<ChatMessage>> {
            return MutableStateFlow(ArrayList())
        }

        override suspend fun addMessage(message: ChatMessage) {
        }
    }
    val emptyChatService = object: ChatService {
        override fun setService(apiKey: String, baseUrl: String, timeoutMillis: Long, proxy: String?) {
        }

        override fun setLLMConfig(configs: Map<String, Any>) {
        }

        override suspend fun sendMessage(message: ChatMessage): Result<ChatMessage> = suspendRunCatching(
            Dispatchers.Default) {
            ChatMessage(role = ChatMessage.ROLE_BOT, content = "this is reply")
        }
    }
    val emptyAllAppsRepository = object: AppsRepository {
        override suspend fun getApps(): Map<String, PackageInfo> {
            return emptyMap()
        }

        override fun getAppsStream(): Flow<Map<String, PackageInfo>> {
            return MutableStateFlow(HashMap())
        }
    }
    val emptySettingDataSource = object: SettingDataSource {
        override suspend fun getConfig(): ChatConfig {
            return ChatConfig()
        }

        override fun getConfigStream(): Flow<ChatConfig> {
            return MutableStateFlow(ChatConfig())
        }

        override suspend fun saveConfig(config: ChatConfig) {
        }
    }
    GptIntentLauncherTheme {
        App(
            NavigationViewModel(),
            ChatScreenViewModel(emptyChatRepository, emptyChatService),
            AllAppsScreenViewModel(emptyAllAppsRepository),
            SettingScreenViewModel(emptyChatService, emptySettingDataSource)
        )
    }
}
package io.github.wangmuy.gptintentlauncher

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.github.wangmuy.gptintentlauncher.chat.ChatScreenViewModel
import io.github.wangmuy.gptintentlauncher.setting.SettingScreenViewModel
import io.github.wangmuy.gptintentlauncher.ui.App
import io.github.wangmuy.gptintentlauncher.ui.NavigationViewModel
import io.github.wangmuy.gptintentlauncher.ui.theme.GptIntentLauncherTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val navigationViewModel: NavigationViewModel by inject()
    private val chatViewModel: ChatScreenViewModel by inject()
    private val settingViewModel: SettingScreenViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            GptIntentLauncherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(navigationViewModel, chatViewModel, settingViewModel)
                }
            }
        }
    }
}

package io.github.wangmuy.gptintentlauncher

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.wangmuy.gptintentlauncher.data.source.InMemoryChatRepository
import io.github.wangmuy.gptintentlauncher.service.LangChainService
import io.github.wangmuy.gptintentlauncher.setting.SettingRepository
import io.github.wangmuy.gptintentlauncher.ui.App
import io.github.wangmuy.gptintentlauncher.ui.theme.GptIntentLauncherTheme

class MainActivity : ComponentActivity() {
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
                    App(InMemoryChatRepository(), LangChainService(), SettingRepository())
                }
            }
        }
    }
}

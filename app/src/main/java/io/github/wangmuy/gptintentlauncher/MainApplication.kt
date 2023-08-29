package io.github.wangmuy.gptintentlauncher

import android.app.Application
import io.github.wangmuy.gptintentlauncher.allapps.allAppsModule
import io.github.wangmuy.gptintentlauncher.chat.chatModule
import io.github.wangmuy.gptintentlauncher.data.dataModule
import io.github.wangmuy.gptintentlauncher.service.chatServiceModule
import io.github.wangmuy.gptintentlauncher.setting.settingModule
import io.github.wangmuy.gptintentlauncher.ui.navigationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    private val appModules = listOf(
        coroutinesModule,
        dataModule,
        navigationModule,
        chatServiceModule,
        chatModule,
        allAppsModule,
        settingModule
    )

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModules)
        }
    }
}
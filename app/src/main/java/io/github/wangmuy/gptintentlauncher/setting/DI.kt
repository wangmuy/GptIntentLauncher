package io.github.wangmuy.gptintentlauncher.setting

import io.github.wangmuy.gptintentlauncher.setting.source.SettingDataSource
import io.github.wangmuy.gptintentlauncher.setting.source.SettingRepository
import org.koin.dsl.module

val settingModule = module {
    single<SettingDataSource> { SettingRepository(get()) }
    factory { SettingScreenViewModel(get(), get()) }
}
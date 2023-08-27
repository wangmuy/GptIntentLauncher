package io.github.wangmuy.gptintentlauncher.setting

import org.koin.dsl.module

val settingModule = module {
    single<SettingDataSource> { SettingRepository() }
    factory { SettingScreenViewModel(get(), get()) }
}
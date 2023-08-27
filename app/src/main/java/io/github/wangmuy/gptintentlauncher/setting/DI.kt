package io.github.wangmuy.gptintentlauncher.setting

import org.koin.dsl.module

val settingModule = module {
    single<SettingDataSource> { SettingRepository(get()) }
    factory { SettingScreenViewModel(get(), get()) }
}
package io.github.wangmuy.gptintentlauncher.ui

import org.koin.dsl.module

val navigationModule = module {
    factory { NavigationViewModel() }
}
package io.github.wangmuy.gptintentlauncher.chat

import io.github.wangmuy.gptintentlauncher.data.REPO_DEFAULT
import org.koin.core.qualifier.named
import org.koin.dsl.module

val chatModule = module {
    factory { ChatScreenViewModel(get(qualifier = named(REPO_DEFAULT)), get()) }
}
package io.github.wangmuy.gptintentlauncher.service

import org.koin.dsl.module

val chatServiceModule = module {
    single<ChatService> { LangChainService() }
}
package io.github.wangmuy.gptintentlauncher.database

import org.koin.dsl.module

val databaseModule = module {
    single {
        ChatDatabase.newInstance(get())
    }
}
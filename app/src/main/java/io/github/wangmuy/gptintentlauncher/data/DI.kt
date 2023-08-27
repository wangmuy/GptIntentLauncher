package io.github.wangmuy.gptintentlauncher.data

import io.github.wangmuy.gptintentlauncher.data.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.DefaultChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.InMemoryChatRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val REPO_DEFAULT = "default"
const val REPO_INMEMORY = "inMemory"

val dataModule = module {
    single<ChatRepository>(named(REPO_DEFAULT)) { DefaultChatRepository() }
    single<ChatRepository>(named(REPO_INMEMORY)) { InMemoryChatRepository() }
}
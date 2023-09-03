package io.github.wangmuy.gptintentlauncher.chat

import io.github.wangmuy.gptintentlauncher.DEFAULT
import io.github.wangmuy.gptintentlauncher.IO
import io.github.wangmuy.gptintentlauncher.database.ChatDatabase
import io.github.wangmuy.gptintentlauncher.chat.service.ChatService
import io.github.wangmuy.gptintentlauncher.chat.service.LangChainService
import io.github.wangmuy.gptintentlauncher.chat.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.chat.source.DefaultChatRepository
import io.github.wangmuy.gptintentlauncher.chat.source.InMemoryChatRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val REPO_INMEMORY = "inMemory"

val chatDataModule = module {
    single { get<ChatDatabase>().chatMessageDao() }
    single<ChatRepository>(named(DEFAULT)) {
        DefaultChatRepository(get(), get(named(IO)), get(named(DEFAULT)))
    }
    single<ChatRepository>(named(REPO_INMEMORY)) { InMemoryChatRepository() }
}

val chatServiceModule = module {
    single<ChatService> { LangChainService(get(), get(named(DEFAULT)), get(), get(named(DEFAULT))) }
}

val chatModule = module {
    factory { ChatScreenViewModel(get(qualifier = named(DEFAULT)), get(), get()) }
}

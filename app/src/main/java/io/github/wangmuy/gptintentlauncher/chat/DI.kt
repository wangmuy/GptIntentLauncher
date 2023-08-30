package io.github.wangmuy.gptintentlauncher.chat

import androidx.room.Room
import io.github.wangmuy.gptintentlauncher.DEFAULT
import io.github.wangmuy.gptintentlauncher.IO
import io.github.wangmuy.gptintentlauncher.chat.service.ChatService
import io.github.wangmuy.gptintentlauncher.chat.service.LangChainService
import io.github.wangmuy.gptintentlauncher.chat.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.chat.source.DefaultChatRepository
import io.github.wangmuy.gptintentlauncher.chat.source.InMemoryChatRepository
import io.github.wangmuy.gptintentlauncher.chat.source.local.ChatDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val REPO_INMEMORY = "inMemory"

val dataModule = module {
    single {
        Room.databaseBuilder(
            get(), ChatDatabase::class.java, ChatDatabase.DB_NAME)
            .build()
    }
    single { get<ChatDatabase>().chatMessageDao() }
    single<ChatRepository>(named(DEFAULT)) {
        DefaultChatRepository(get(), get(named(IO)), get(named(DEFAULT)))
    }
    single<ChatRepository>(named(REPO_INMEMORY)) { InMemoryChatRepository() }
}

val chatServiceModule = module {
    single<ChatService> { LangChainService() }
}

val chatModule = module {
    factory { ChatScreenViewModel(get(qualifier = named(DEFAULT)), get()) }
}

package io.github.wangmuy.gptintentlauncher.data

import androidx.room.Room
import io.github.wangmuy.gptintentlauncher.DEFAULT
import io.github.wangmuy.gptintentlauncher.IO
import io.github.wangmuy.gptintentlauncher.data.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.DefaultChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.InMemoryChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.local.ChatDatabase
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
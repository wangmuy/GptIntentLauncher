package io.github.wangmuy.gptintentlauncher.data

import androidx.room.Room
import io.github.wangmuy.gptintentlauncher.DISPATCHER_IO
import io.github.wangmuy.gptintentlauncher.data.source.ChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.DefaultChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.InMemoryChatRepository
import io.github.wangmuy.gptintentlauncher.data.source.local.ChatDatabase
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val REPO_DEFAULT = "default"
const val REPO_INMEMORY = "inMemory"

val dataModule = module {
    single {
        Room.databaseBuilder(
            get(), ChatDatabase::class.java, ChatDatabase.DB_NAME)
            .build()
    }
    single { get<ChatDatabase>().chatMessageDao() }
    single<ChatRepository>(named(REPO_DEFAULT)) {
        DefaultChatRepository(get(), get(named(DISPATCHER_IO)))
    }
    single<ChatRepository>(named(REPO_INMEMORY)) { InMemoryChatRepository() }
}
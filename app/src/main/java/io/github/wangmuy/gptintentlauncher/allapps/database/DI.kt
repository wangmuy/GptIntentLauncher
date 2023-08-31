package io.github.wangmuy.gptintentlauncher.allapps.database

import androidx.room.Room
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(), ChatDatabase::class.java, ChatDatabase.DB_NAME)
            .build()
    }
}
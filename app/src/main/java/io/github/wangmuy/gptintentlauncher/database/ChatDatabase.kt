package io.github.wangmuy.gptintentlauncher.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.wangmuy.gptintentlauncher.allapps.source.LocalPackageStoreInfo
import io.github.wangmuy.gptintentlauncher.allapps.source.PackageStoreInfoDao
import io.github.wangmuy.gptintentlauncher.chat.source.local.ChatMessageDao
import io.github.wangmuy.gptintentlauncher.chat.source.local.LocalChatMessage

@Database(
    entities = [
        LocalChatMessage::class,
        LocalPackageStoreInfo::class
    ],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class ChatDatabase: RoomDatabase() {
    companion object {
        val DB_NAME = "chat.db"

        fun newInstance(context: Context): ChatDatabase {
            return Room.databaseBuilder(context, ChatDatabase::class.java, DB_NAME)
                .build()
        }
    }

    abstract fun chatMessageDao(): ChatMessageDao

    abstract fun packageStoreInfoDao(): PackageStoreInfoDao
}

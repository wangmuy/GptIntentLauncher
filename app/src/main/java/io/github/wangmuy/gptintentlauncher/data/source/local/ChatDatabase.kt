package io.github.wangmuy.gptintentlauncher.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LocalChatMessage::class],
    version = 1,
    exportSchema = false
)
abstract class ChatDatabase: RoomDatabase() {
    companion object {
        val DB_NAME = "chat.db"
    }
    abstract fun chatMessageDao(): ChatMessageDao
}

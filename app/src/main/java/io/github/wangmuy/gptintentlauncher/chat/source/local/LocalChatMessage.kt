package io.github.wangmuy.gptintentlauncher.chat.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatMessage")
data class LocalChatMessage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Int = 0,

    @ColumnInfo(name = "role")
    var role: String = "",

    @ColumnInfo(name = "content")
    var content: String = "",

    @ColumnInfo(name = "createdAt")
    val createdAt: Long = System.currentTimeMillis()
)
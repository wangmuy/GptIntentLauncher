package io.github.wangmuy.gptintentlauncher.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatMessage")
data class LocalChatMessage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    val id: Int,

    @ColumnInfo(name = "role")
    var role: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name = "createdAt")
    val createdAt: Long
)
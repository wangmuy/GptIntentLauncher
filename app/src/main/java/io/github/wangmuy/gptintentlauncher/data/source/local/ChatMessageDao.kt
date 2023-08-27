package io.github.wangmuy.gptintentlauncher.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {

    @Query("SELECT * FROM chatMessage")
    fun observeAll(): Flow<List<LocalChatMessage>>

    @Query("SELECT * from chatMessage")
    suspend fun getAll(): List<LocalChatMessage>

    @Insert
    suspend fun insert(msg: LocalChatMessage)
}
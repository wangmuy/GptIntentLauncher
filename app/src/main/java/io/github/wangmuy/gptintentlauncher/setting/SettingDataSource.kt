package io.github.wangmuy.gptintentlauncher.setting

import io.github.wangmuy.gptintentlauncher.data.model.ChatConfig
import kotlinx.coroutines.flow.Flow

interface SettingDataSource {
    suspend fun getConfig(): ChatConfig
    fun getConfigStream(): Flow<ChatConfig>
    suspend fun saveConfig(config: ChatConfig)
}
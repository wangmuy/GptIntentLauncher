package io.github.wangmuy.gptintentlauncher.setting.source

import io.github.wangmuy.gptintentlauncher.setting.model.ChatConfig
import kotlinx.coroutines.flow.Flow

interface SettingDataSource {
    suspend fun getConfig(): ChatConfig
    fun getConfigStream(): Flow<ChatConfig>
    suspend fun saveConfig(config: ChatConfig)
}
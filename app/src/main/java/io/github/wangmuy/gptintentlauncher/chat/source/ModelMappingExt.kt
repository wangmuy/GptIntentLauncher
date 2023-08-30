package io.github.wangmuy.gptintentlauncher.chat.source

import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.chat.source.local.LocalChatMessage

fun ChatMessage.toLocal() = LocalChatMessage(
    id = id,
    role = role,
    content = content,
    createdAt = timeMs
)

fun List<ChatMessage>.toLocal() = map(ChatMessage::toLocal)

fun LocalChatMessage.toExternal() = ChatMessage(
    id = id,
    role = role,
    content = content,
    timeMs = createdAt
)

// Note: JvmName is used to provide a unique name for each extension function with the same name.
// Without this, type erasure will cause compiler errors because these methods will have the same
// signature on the JVM.
@JvmName("localToExternal")
fun List<LocalChatMessage>.toExternal() = map(LocalChatMessage::toExternal)

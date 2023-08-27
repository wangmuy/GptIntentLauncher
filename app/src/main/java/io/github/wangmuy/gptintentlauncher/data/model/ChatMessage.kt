package io.github.wangmuy.gptintentlauncher.data.model

open class ChatMessage(
    val id: Int = 0,
    val role: String,
    val content: String,
    val timeMs: Long = System.currentTimeMillis()
) {
    companion object {
        const val ROLE_ME = "me"
        const val ROLE_SYSTEM = "system"
        const val ROLE_BOT = "bot"
        const val ROLE_APP = "app"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatMessage

        if (id != other.id) return false
        if (role != other.role) return false
        if (content != other.content) return false
        if (timeMs != other.timeMs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + role.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + timeMs.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChatMessage(id=$id, role='$role', content='$content', timeMs=$timeMs)"
    }
}
package io.github.wangmuy.gptintentlauncher.chat.service.tools

import android.util.Log
import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import io.github.wangmuy.gptintentlauncher.allapps.source.AppsRepository
import io.github.wangmuy.gptintentlauncher.chat.model.ChatMessage
import io.github.wangmuy.gptintentlauncher.chat.service.LangChainService
import io.github.wangmuy.gptintentlauncher.chat.source.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class PackageTool(
    val packageInfo: PackageInfo
): BaseTool(
    name = packageInfo.pkgName,
    description = getDescription(packageInfo)
) {
    companion object {
        private const val TAG = "PackageTool$DEBUG_TAG"
        private const val INDENT = 2

        private fun pkgDesc(packageInfo: PackageInfo): String {
            val commonDesc = ""// "app installed on this android phone."
            return packageInfo.storeInfo?.let { "$commonDesc ${it.title}. ${it.summary}" } ?: commonDesc
        }

        private fun activitiesDesc(packageInfo: PackageInfo): String {
            val activities = JSONArray()
            for (activity in packageInfo.launcherActivities) {
                activities.put(JSONObject().apply {
                    put("label", activity.label)
//                    put("component", activity.componentName)
                })
            }
            return activities.toString(INDENT)
        }

        private fun shortcutsDesc(packageInfo: PackageInfo): String {
            val shortcuts = JSONArray()
            for (shortcut in packageInfo.shortcutInfos) {
                shortcuts.put(JSONObject().apply {
                    put("id", shortcut.id)
                    put("shortLabel", shortcut.shortLabel)
                    put("longLabel", shortcut.longLabel)
                    val categories = JSONArray()
                    for (cat in shortcut.categories) {
                        categories.put(cat)
                    }
                    put("categories", categories)
                })
            }
            return shortcuts.toString(INDENT)
        }

        fun getDescription(packageInfo: PackageInfo): String {
            // JSONObject output order may disturb llm, so write directly here
            return """
{
  "name": "${packageInfo.pkgName}",
  "type": "app",
  "description": "${pkgDesc(packageInfo)}",
  "activities": ${activitiesDesc(packageInfo)},
  "shortcuts": ${shortcutsDesc(packageInfo)}
}
            """.trimIndent()
        }
    }

    override fun onRun(toolInput: String, args: Map<String, Any>?): String {
        val appsRepository = args?.get(LangChainService.KEY_APPS_REPOSITORY) as? AppsRepository
        val chatRepository = args?.get(LangChainService.KEY_CHAT_REPOSITORY) as? ChatRepository
        val scope = args?.get(LangChainService.KEY_COROUTINE_SCOPE) as? CoroutineScope
        Log.d(TAG, "onRun tid=${Thread.currentThread().id}, args=$args, appRepository=$appsRepository, chatRepository=$chatRepository, scope=$scope")
        var output = "started package ${packageInfo.pkgName}"
        if (appsRepository != null) {
            try {
                val params = JSONObject(toolInput).getJSONObject("params")
                val type = params.getString("intent")
                val value = params.getString("value")
                when (type) {
                    "activity" -> {
                        // value is label
                        val detail = packageInfo.launcherActivities.firstOrNull { it.label == value }
                        if (detail != null) {
                            appsRepository.startActivity(detail)
                            output += ", activity=$value"
                        } else {
                            throw IllegalStateException("component not found value=$value")
                        }
                    }
                    else -> {
                        throw IllegalStateException("type not supported yet type=$type") // todo
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "onRun failed", e)
                output = e.toString()
                scope?.launch {
                    chatRepository?.addMessage(ChatMessage(
                        role = ChatMessage.ROLE_APP,
                        content = "$e\n${e.stackTraceToString()}"
                    ))
                }
            }
        }
        return output
    }
}
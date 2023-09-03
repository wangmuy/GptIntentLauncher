package io.github.wangmuy.gptintentlauncher.chat.service.tools

import com.wangmuy.llmchain.tool.BaseTool
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import org.json.JSONArray
import org.json.JSONObject

class PackageTool(
    val packageInfo: PackageInfo
): BaseTool(
    name = packageInfo.pkgName,
    description = getDescription(packageInfo)
) {
    companion object {
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
                    put("component", activity.componentName)
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
  "description": "${pkgDesc(packageInfo)}",
  "activities": ${activitiesDesc(packageInfo)},
  "shortcuts": ${shortcutsDesc(packageInfo)},
  "parameters": {
    "type": "object",
    "properties": {
      "intent": {
        "type": "string",
        "description": "The selected intent to start, must be activity or shortcut",
        "enum": ["activity", "shortcut"]
      },
      "value": {
        "type": "string",
        "description": "if type is activity, this is the corresponding component value. if type is shortcut, this is the corresponding shortcut id"
      }
    },
    "required": ["type", "value"]
  }
}
            """.trimIndent()
        }
    }

    override fun onRun(toolInput: String, args: Map<String, Any>?): String {
        return "started package ${packageInfo.pkgName}"
    }
}
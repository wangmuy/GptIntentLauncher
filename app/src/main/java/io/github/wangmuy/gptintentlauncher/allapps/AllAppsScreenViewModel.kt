package io.github.wangmuy.gptintentlauncher.allapps

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.wangmuy.gptintentlauncher.Const.DEBUG_TAG
import io.github.wangmuy.gptintentlauncher.allapps.model.ActivityInfo
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import io.github.wangmuy.gptintentlauncher.allapps.service.AppStoreService
import io.github.wangmuy.gptintentlauncher.allapps.source.AppsRepository
import io.github.wangmuy.gptintentlauncher.util.Async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AllAppsScreenViewModel(
    private val allAppsRepository: AppsRepository,
    private val appStoreService: AppStoreService
): ViewModel() {
    companion object {
        private const val TAG = "AllAppsScreenViewModel$DEBUG_TAG"
    }

    val currentUiState: StateFlow<Async<Map<String, PackageInfo>>> = allAppsRepository
        .getAppsStream().map { Async.Success(it) }
        .catch<Async<Map<String, PackageInfo>>> { emit(Async.Error("error loading all apps")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = Async.Loading
        )

    fun onStartActivity(activityInfo: ActivityInfo, viewBounds: Rect?, opts: Bundle? = null) {
        Log.d(TAG, "onStartActivity $activityInfo, viewBounds=$viewBounds, opts=$opts")
        allAppsRepository.startActivity(activityInfo, viewBounds, opts)
    }
}
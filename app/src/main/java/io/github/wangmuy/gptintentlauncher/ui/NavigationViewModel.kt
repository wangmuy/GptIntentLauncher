package io.github.wangmuy.gptintentlauncher.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel: ViewModel() {
    companion object {
        const val SCREEN_CHAT = 0
        const val SCREEN_ALLAPPS = 1
        const val SCREEN_SETTING = 2
    }

    val navigationState: MutableStateFlow<NavigationUiState> = MutableStateFlow(NavigationUiState())

    fun onSelectScreen(screenId: Int) {
        navigationState.update {
            it.copy(currentScreenId = screenId)
        }
    }
}

data class NavigationUiState(
    val currentScreenId: Int = NavigationViewModel.SCREEN_CHAT
)
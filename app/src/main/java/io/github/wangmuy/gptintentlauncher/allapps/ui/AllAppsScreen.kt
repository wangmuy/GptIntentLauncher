package io.github.wangmuy.gptintentlauncher.allapps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.wangmuy.gptintentlauncher.allapps.AllAppsScreenViewModel
import io.github.wangmuy.gptintentlauncher.allapps.model.PackageInfo
import io.github.wangmuy.gptintentlauncher.util.Async

@Composable
fun AllAppsScreen(
    contentPadding: PaddingValues,
    viewModel: AllAppsScreenViewModel
) {
    val uiState by viewModel.currentUiState.collectAsState()
    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize().padding(contentPadding)
    ) {
        when (uiState) {
            Async.Loading -> {
                Text(text = "Loading...")
            }

            is Async.Error -> {
                Text(text = (uiState as Async.Error).errorMessage)
            }

            is Async.Success -> {
                val packages = (uiState as Async.Success<Map<String, PackageInfo>>).data
                val launcherActivities = packages.values.flatMap { it.launcherActivities }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    state = listState) {
                    items(launcherActivities, key = { it.activityInfo.componentName }) {
                        LaunchCard(
                            activityInfo = it,
                            onClick = viewModel::onStartActivity
                        )
                    }
                }
            }
        }
    }
}
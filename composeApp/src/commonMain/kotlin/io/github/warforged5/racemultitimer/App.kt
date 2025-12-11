package io.github.warforged5.racemultitimer

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.racetimer.app.ui.screens.RaceTimerScreen
import com.racetimer.app.ui.theme.RaceTimerTheme
import io.github.warforged5.racemultitimer.data.SettingsRepository
import io.github.warforged5.racemultitimer.viewmodel.RaceTimerViewModel

/**
 * Main application composable
 * Sets up theme and main screen with ViewModel
 */
@Composable
fun App(
    settingsRepository: SettingsRepository? = null
) {
    val viewModel: RaceTimerViewModel = viewModel { RaceTimerViewModel(settingsRepository) }
    val state by viewModel.state.collectAsState()

    RaceTimerTheme(appTheme = state.theme, palette = state.colorPalette) {
        RaceTimerScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}
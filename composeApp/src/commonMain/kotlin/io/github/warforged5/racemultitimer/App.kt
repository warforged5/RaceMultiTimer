package io.github.warforged5.racemultitimer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.racetimer.app.ui.screens.RaceTimerScreen
import com.racetimer.app.ui.theme.RaceTimerTheme
import io.github.warforged5.racemultitimer.viewmodel.RaceTimerViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import racemultitimer.composeapp.generated.resources.Res
import racemultitimer.composeapp.generated.resources.compose_multiplatform
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

/**
 * Main application composable
 * Sets up theme and main screen with ViewModel
 */
@Composable
fun App(
    viewModel: RaceTimerViewModel = viewModel { RaceTimerViewModel() }
) {
    val state by viewModel.state.collectAsState()

    RaceTimerTheme(appTheme = state.theme, palette = state.colorPalette) {
        RaceTimerScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}
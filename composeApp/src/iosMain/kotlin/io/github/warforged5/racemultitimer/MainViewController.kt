package io.github.warforged5.racemultitimer

import androidx.compose.ui.window.ComposeUIViewController
import io.github.warforged5.racemultitimer.data.SettingsRepository
import io.github.warforged5.racemultitimer.data.createDataStore

private val settingsRepository by lazy {
    SettingsRepository(createDataStore())
}

fun MainViewController() = ComposeUIViewController { App(settingsRepository = settingsRepository) }
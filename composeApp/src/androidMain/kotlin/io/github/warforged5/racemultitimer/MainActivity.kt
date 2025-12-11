package io.github.warforged5.racemultitimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.github.warforged5.racemultitimer.data.SettingsRepository
import io.github.warforged5.racemultitimer.data.createDataStore

class MainActivity : ComponentActivity() {

    private val settingsRepository by lazy {
        SettingsRepository(createDataStore(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(settingsRepository = settingsRepository)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
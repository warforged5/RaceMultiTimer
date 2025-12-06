package io.github.warforged5.racemultitimer

import android.os.Build
import io.github.warforged5.racemultitimer.AndroidPlatform
import io.github.warforged5.racemultitimer.Platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun isAndroid(): Boolean = true;
package io.github.warforged5.racemultitimer

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun isAndroid(): Boolean

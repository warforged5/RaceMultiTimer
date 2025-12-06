package io.github.warforged5.racemultitimer.domain

import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.Clock.System.now
import kotlin.time.ExperimentalTime
/**
 * Represents a single lap time with optional split information
 */
@Serializable
data class LapTime @OptIn(ExperimentalTime::class) constructor(
    val lapNumber: Int,
    val elapsedTime: Long, // Total elapsed time when this lap was recorded (milliseconds)
    val lapDuration: Long, // Duration of this specific lap (milliseconds)
    val timestamp: Long = kotlin.time.Clock.System.now().toEpochMilliseconds()
)

/**
 * Represents a single racing lane with its timer state
 */
@Serializable
data class Lane(
    val id: String,
    val name: String,
    val position: Int,
    val isRunning: Boolean = false,
    val startTime: Long? = null,
    val pausedElapsedTime: Long = 0L, // Time elapsed before last pause
    val laps: List<LapTime> = emptyList(),
    val color: LaneColor = LaneColor.DEFAULT
) {
    @OptIn(ExperimentalTime::class)
    val currentElapsedTime: Long
        get() = if (isRunning && startTime != null) {
            pausedElapsedTime + (Clock.System.now().toEpochMilliseconds() - startTime)
        } else {
            pausedElapsedTime
        }

    val lastLapTime: Long
        get() = laps.lastOrNull()?.elapsedTime ?: 0L

    val currentLapDuration: Long
        get() = currentElapsedTime - lastLapTime

    val lapCount: Int
        get() = laps.size

    val averageLapTime: Long
        get() = if (laps.isNotEmpty()) {
            laps.map { it.lapDuration }.average().toLong()
        } else 0L

    val bestLapTime: Long?
        get() = laps.minOfOrNull { it.lapDuration }

    val worstLapTime: Long?
        get() = laps.maxOfOrNull { it.lapDuration }
}

/**
 * Available lane colors for visual distinction
 */
@Serializable
enum class LaneColor(val primary: Long, val secondary: Long, name: String) {
    DEFAULT(0xFF6750A4, 0xFFEADDFF, "Purple"),
    RED(0xFFDC362E, 0xFFFFDAD6, "Red"),
    ORANGE(0xFFE8590C, 0xFFFFDBC9, "Orange"),
    YELLOW(0xFFC49102, 0xFFFFE08D, "Yellow"),
    GREEN(0xFF2E7D32, 0xFFC8E6C9, "Green"),
    TEAL(0xFF00897B, 0xFFB2DFDB, "Teal"),
    BLUE(0xFF1976D2, 0xFFBBDEFB, "Blue"),
    INDIGO(0xFF303F9F, 0xFFC5CAE9, "Indigo"),
    PINK(0xFFC2185B, 0xFFF8BBD9, "Pink"),
    CYAN(0xFF0097A7, 0xFFB2EBF2, "Cyan")
}

/**
 * Timer preset for quick race setups
 */
@Serializable
data class TimerPreset @OptIn(ExperimentalTime::class) constructor(
    val id: String,
    val name: String,
    val laneNames: List<String>,
    val laneColors: List<LaneColor>,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds()
)

/**
 * App theme options
 */
@Serializable
enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK,
    DYNAMIC // Material You / Dynamic color (Android only)
}

/**
 * Overall race state
 */
@Serializable
enum class RaceState {
    IDLE,
    COUNTDOWN,
    RUNNING,
    PAUSED,
    FINISHED
}

/**
 * Race results for sharing
 */
@Serializable
data class RaceResults(
    val raceName: String,
    val date: Long,
    val lanes: List<LaneResult>,
    val totalDuration: Long
)

@Serializable
data class LaneResult(
    val name: String,
    val position: Int,
    val finalTime: Long,
    val laps: List<LapTime>,
    val averageLap: Long,
    val bestLap: Long?,
    val worstLap: Long?
)

/**
 * Utility functions for time formatting
 */
object TimeUtils {
    fun formatTime(millis: Long, showMillis: Boolean = true): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val ms = (millis % 1000) / 10 // Show centiseconds

        return buildString {
            if (hours > 0) {
                append(hours.toString().padStart(2, '0'))
                append(":")
            }
            append(minutes.toString().padStart(2, '0'))
            append(":")
            append(seconds.toString().padStart(2, '0'))
            if (showMillis) {
                append(".")
                append(ms.toString().padStart(2, '0'))
            }
        }
    }

    fun formatLapTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val ms = (millis % 1000) / 10

        return if (minutes > 0) {
            "${minutes}:${seconds.toString().padStart(2, '0')}.${ms.toString().padStart(2, '0')}"
        } else {
            "${seconds}.${ms.toString().padStart(2, '0')}s"
        }
    }

    fun formatDuration(millis: Long): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}m ${seconds}s"
            minutes > 0 -> "${minutes}m ${seconds}s"
            else -> "${seconds}s"
        }
    }
}

/**
 * Generate unique IDs
 */
object IdGenerator {
    private var counter = 0L

    @OptIn(ExperimentalTime::class)
    fun generateId(): String {
        counter++
        return "lane_${Clock.System.now().toEpochMilliseconds()}_$counter"
    }

    @OptIn(ExperimentalTime::class)
    fun generatePresetId(): String {
        counter++
        return "preset_${Clock.System.now().toEpochMilliseconds()}_$counter"
    }

    @OptIn(ExperimentalTime::class)
    fun generatePaceId(): String {
        counter++
        return "pace_${Clock.System.now().toEpochMilliseconds()}_$counter"
    }

    @OptIn(ExperimentalTime::class)
    fun generateNoteId(): String {
        counter++
        return "note_${Clock.System.now().toEpochMilliseconds()}_$counter"
    }
}

/**
 * Standard race events with their distances
 */
@Serializable
enum class RaceEvent(val displayName: String, val distanceMeters: Int) {
    M100("100m", 100),
    M200("200m", 200),
    M400("400m", 400),
    M800("800m", 800),
    M1500("1500m", 1500),
    M1600("1 Mile", 1609),
    M3000("3000m", 3000),
    M3200("2 Mile", 3219),
    M5000("5K", 5000),
    M10000("10K", 10000),
    HALF_MARATHON("Half Marathon", 21097),
    MARATHON("Marathon", 42195)
}

/**
 * Split interval options for pace markers
 */
@Serializable
enum class SplitInterval(val displayName: String, val distanceMeters: Int) {
    M100("100m", 100),
    M200("200m", 200),
    M400("400m", 400),
    MILE("Mile", 1609),
    KM("1K", 1000)
}

/**
 * Expected pace marker showing split times for a target pace
 */
@Serializable
data class PaceMarker(
    val id: String,
    val event: RaceEvent,
    val splitInterval: SplitInterval,
    val targetTime: Long, // Total target time in milliseconds
    val isVisible: Boolean = true
) {
    /**
     * Calculate split times based on even pacing
     */
    fun getSplits(): List<PaceSplit> {
        val splits = mutableListOf<PaceSplit>()
        val totalDistance = event.distanceMeters
        val splitDistance = splitInterval.distanceMeters

        // Calculate how many full splits we have
        val numFullSplits = totalDistance / splitDistance
        val remainingDistance = totalDistance % splitDistance

        // Time per meter
        val timePerMeter = targetTime.toDouble() / totalDistance

        var accumulatedDistance = 0

        // Add intermediate splits
        for (i in 1..numFullSplits) {
            accumulatedDistance += splitDistance
            val splitTime = (accumulatedDistance * timePerMeter).toLong()

            // Don't add if this is the final distance
            if (accumulatedDistance < totalDistance) {
                splits.add(PaceSplit(
                    label = formatSplitLabel(splitInterval, i),
                    distance = accumulatedDistance,
                    time = splitTime,
                    isFinal = false
                ))
            }
        }

        // Add final split (the event itself)
        splits.add(PaceSplit(
            label = event.displayName,
            distance = totalDistance,
            time = targetTime,
            isFinal = true
        ))

        return splits
    }

    private fun formatSplitLabel(interval: SplitInterval, count: Int): String {
        return when (interval) {
            SplitInterval.MILE -> "$count"
            SplitInterval.KM -> "$count"
            else -> {
                // For meter intervals like 100m, 200m, 400m - show accumulated distance
                val distanceValue = interval.displayName.replace("m", "").toIntOrNull()
                if (distanceValue != null) {
                    "(${distanceValue * count})"
                } else {
                    "(${interval.displayName})"
                }
            }
        }
    }
}

@Serializable
data class PaceSplit(
    val label: String,
    val distance: Int,
    val time: Long,
    val isFinal: Boolean
)

/**
 * Coach's note bar
 */
@Serializable
data class CoachNote(
    val id: String,
    val text: String,
    val isVisible: Boolean = true
)
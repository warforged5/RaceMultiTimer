package io.github.warforged5.racemultitimer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.warforged5.racemultitimer.domain.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Main state for the Race Timer application
 */
data class RaceTimerState(
    val lanes: List<Lane> = emptyList(),
    val raceState: RaceState = RaceState.IDLE,
    val countdownSeconds: Int = 3,
    val countdownRemaining: Int = 0,
    val restTimerSeconds: Int = 60,
    val restTimerRemaining: Int = 0,
    val isRestTimerRunning: Boolean = false,
    val customRestPresets: List<Int> = emptyList(), // Custom rest timer presets in seconds
    val showLapsInCards: Boolean = true, // Toggle for showing laps in lane cards
    val theme: AppTheme = AppTheme.SYSTEM,
    val colorPalette: String = "indigo",
    val presets: List<TimerPreset> = emptyList(),
    val paceMarkers: List<PaceMarker> = emptyList(),
    val coachNotes: List<CoachNote> = emptyList(),
    val expandedLaneId: String? = null,
    val showThemeSelector: Boolean = false,
    val showMenuDropdown: Boolean = false,
    val showCountdownDialog: Boolean = false,
    val showRestTimerDialog: Boolean = false,
    val showSavePresetDialog: Boolean = false,
    val showLoadPresetDialog: Boolean = false,
    val showShareDialog: Boolean = false,
    val showRenameLaneDialog: String? = null,
    val showAddPaceDialog: Boolean = false,
    val showAddNoteDialog: Boolean = false,
    val showManageMarkersDialog: Boolean = false,
    val currentTimeMillis: Long = 0L // Triggers recomposition efficiently
)

sealed class RaceTimerEvent {
    data object AddLane : RaceTimerEvent()
    data class RemoveLane(val laneId: String) : RaceTimerEvent()
    data class RenameLane(val laneId: String, val newName: String) : RaceTimerEvent()
    data class ReorderLanes(val fromIndex: Int, val toIndex: Int) : RaceTimerEvent()

    data object StartAll : RaceTimerEvent()
    data object StopAll : RaceTimerEvent()
    data object LapAll : RaceTimerEvent()
    data class LapLane(val laneId: String) : RaceTimerEvent()
    data class StartLane(val laneId: String) : RaceTimerEvent()
    data class StopLane(val laneId: String) : RaceTimerEvent()
    data class ResetLane(val laneId: String) : RaceTimerEvent()

    data object ResetAllLanes : RaceTimerEvent()
    data object ResetEverything : RaceTimerEvent()
    data class StartCountdown(val seconds: Int) : RaceTimerEvent()
    data class StartRestTimer(val seconds: Int) : RaceTimerEvent()
    data object StopRestTimer : RaceTimerEvent()
    data class AddCustomRestPreset(val seconds: Int) : RaceTimerEvent()
    data class RemoveCustomRestPreset(val seconds: Int) : RaceTimerEvent()

    data class SavePreset(val name: String) : RaceTimerEvent()
    data class LoadPreset(val presetId: String) : RaceTimerEvent()
    data class DeletePreset(val presetId: String) : RaceTimerEvent()

    // Pace marker events
    data class AddPaceMarker(val event: RaceEvent, val splitInterval: SplitInterval, val targetTime: Long) : RaceTimerEvent()
    data class RemovePaceMarker(val markerId: String) : RaceTimerEvent()
    data class TogglePaceMarkerVisibility(val markerId: String) : RaceTimerEvent()

    // Coach note events
    data class AddCoachNote(val text: String) : RaceTimerEvent()
    data class RemoveCoachNote(val noteId: String) : RaceTimerEvent()
    data class UpdateCoachNote(val noteId: String, val text: String) : RaceTimerEvent()
    data class ToggleNoteVisibility(val noteId: String) : RaceTimerEvent()

    data class SetTheme(val theme: AppTheme) : RaceTimerEvent()
    data class SetColorPalette(val palette: String) : RaceTimerEvent()
    data class ExpandLane(val laneId: String?) : RaceTimerEvent()
    data class ShowDialog(val dialog: DialogType) : RaceTimerEvent()
    data class ShowRenameLaneDialog(val laneId: String) : RaceTimerEvent()
    data object DismissDialog : RaceTimerEvent()
    data object ToggleMenuDropdown : RaceTimerEvent()
    data object ToggleShowLaps : RaceTimerEvent()
}

enum class DialogType {
    THEME_SELECTOR,
    COUNTDOWN,
    REST_TIMER,
    SAVE_PRESET,
    LOAD_PRESET,
    SHARE,
    ADD_PACE,
    ADD_NOTE,
    MANAGE_MARKERS
}

class RaceTimerViewModel : ViewModel() {

    private val _state = MutableStateFlow(RaceTimerState())
    val state: StateFlow<RaceTimerState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private var countdownJob: Job? = null
    private var restTimerJob: Job? = null

    init {
        startHighFrequencyTimer()
    }

    /**
     * High-frequency timer that updates every 10ms for smooth display
     * Only triggers recomposition when timers are running
     */
    @OptIn(ExperimentalTime::class)
    private fun startHighFrequencyTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (isActive) {
                val currentState = _state.value
                val hasRunningTimers = currentState.lanes.any { it.isRunning } ||
                        currentState.raceState == RaceState.COUNTDOWN ||
                        currentState.isRestTimerRunning

                if (hasRunningTimers) {
                    // Update at 100Hz (10ms) for millisecond precision display
                    _state.update {
                        it.copy(currentTimeMillis = Clock.System.now().toEpochMilliseconds())
                    }
                    delay(10)
                } else {
                    // When idle, check less frequently to save battery
                    delay(100)
                }
            }
        }
    }

    fun onEvent(event: RaceTimerEvent) {
        when (event) {
            is RaceTimerEvent.AddLane -> addLane()
            is RaceTimerEvent.RemoveLane -> removeLane(event.laneId)
            is RaceTimerEvent.RenameLane -> renameLane(event.laneId, event.newName)
            is RaceTimerEvent.ReorderLanes -> reorderLanes(event.fromIndex, event.toIndex)

            is RaceTimerEvent.StartAll -> startAll()
            is RaceTimerEvent.StopAll -> stopAll()
            is RaceTimerEvent.LapAll -> lapAll()
            is RaceTimerEvent.LapLane -> lapLane(event.laneId)
            is RaceTimerEvent.StartLane -> startLane(event.laneId)
            is RaceTimerEvent.StopLane -> stopLane(event.laneId)
            is RaceTimerEvent.ResetLane -> resetLane(event.laneId)

            is RaceTimerEvent.ResetAllLanes -> resetAllLanes()
            is RaceTimerEvent.ResetEverything -> resetEverything()
            is RaceTimerEvent.StartCountdown -> startCountdown(event.seconds)
            is RaceTimerEvent.StartRestTimer -> startRestTimer(event.seconds)
            is RaceTimerEvent.StopRestTimer -> stopRestTimer()
            is RaceTimerEvent.AddCustomRestPreset -> addCustomRestPreset(event.seconds)
            is RaceTimerEvent.RemoveCustomRestPreset -> removeCustomRestPreset(event.seconds)

            is RaceTimerEvent.SavePreset -> savePreset(event.name)
            is RaceTimerEvent.LoadPreset -> loadPreset(event.presetId)
            is RaceTimerEvent.DeletePreset -> deletePreset(event.presetId)

            // Pace markers
            is RaceTimerEvent.AddPaceMarker -> addPaceMarker(event.event, event.splitInterval, event.targetTime)
            is RaceTimerEvent.RemovePaceMarker -> removePaceMarker(event.markerId)
            is RaceTimerEvent.TogglePaceMarkerVisibility -> togglePaceMarkerVisibility(event.markerId)

            // Coach notes
            is RaceTimerEvent.AddCoachNote -> addCoachNote(event.text)
            is RaceTimerEvent.RemoveCoachNote -> removeCoachNote(event.noteId)
            is RaceTimerEvent.UpdateCoachNote -> updateCoachNote(event.noteId, event.text)
            is RaceTimerEvent.ToggleNoteVisibility -> toggleNoteVisibility(event.noteId)

            is RaceTimerEvent.SetTheme -> setTheme(event.theme)
            is RaceTimerEvent.SetColorPalette -> setColorPalette(event.palette)
            is RaceTimerEvent.ExpandLane -> expandLane(event.laneId)
            is RaceTimerEvent.ShowDialog -> showDialog(event.dialog)
            is RaceTimerEvent.ShowRenameLaneDialog -> showRenameLaneDialog(event.laneId)
            is RaceTimerEvent.DismissDialog -> dismissDialog()
            is RaceTimerEvent.ToggleMenuDropdown -> toggleMenuDropdown()
            is RaceTimerEvent.ToggleShowLaps -> toggleShowLaps()
        }
    }

    private fun toggleShowLaps() {
        _state.update { it.copy(showLapsInCards = !it.showLapsInCards) }
    }

    private fun addLane() {
        val currentLanes = _state.value.lanes
        val newPosition = currentLanes.size + 1
        val availableColors = LaneColor.entries.filter { color ->
            color !in currentLanes.map { it.color }
        }
        val newColor = availableColors.firstOrNull() ?: LaneColor.entries[newPosition % LaneColor.entries.size]

        val newLane = Lane(
            id = IdGenerator.generateId(),
            name = "Lane $newPosition",
            position = newPosition,
            color = newColor
        )

        _state.update { it.copy(lanes = it.lanes + newLane) }
    }

    private fun removeLane(laneId: String) {
        _state.update { state ->
            val newLanes = state.lanes.filter { it.id != laneId }
                .mapIndexed { index, lane -> lane.copy(position = index + 1) }
            state.copy(
                lanes = newLanes,
                expandedLaneId = if (state.expandedLaneId == laneId) null else state.expandedLaneId
            )
        }
    }

    private fun renameLane(laneId: String, newName: String) {
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (lane.id == laneId) lane.copy(name = newName.trim().ifEmpty { lane.name })
                    else lane
                },
                showRenameLaneDialog = null
            )
        }
    }

    private fun reorderLanes(fromIndex: Int, toIndex: Int) {
        if (fromIndex == toIndex) return
        _state.update { state ->
            val mutableLanes = state.lanes.toMutableList()
            val item = mutableLanes.removeAt(fromIndex)
            mutableLanes.add(toIndex, item)
            val reorderedLanes = mutableLanes.mapIndexed { index, lane ->
                lane.copy(position = index + 1)
            }
            state.copy(lanes = reorderedLanes)
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun startAll() {
        val now = Clock.System.now().toEpochMilliseconds()
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (!lane.isRunning) {
                        lane.copy(isRunning = true, startTime = now)
                    } else lane
                },
                raceState = RaceState.RUNNING
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun stopAll() {
        val now = Clock.System.now().toEpochMilliseconds()
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (lane.isRunning && lane.startTime != null) {
                        val elapsed = lane.pausedElapsedTime + (now - lane.startTime)
                        lane.copy(isRunning = false, startTime = null, pausedElapsedTime = elapsed)
                    } else lane
                },
                raceState = RaceState.PAUSED
            )
        }
    }

    private fun lapAll() {
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (lane.isRunning || lane.pausedElapsedTime > 0) recordLap(lane) else lane
                }
            )
        }
    }

    private fun lapLane(laneId: String) {
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (lane.id == laneId && (lane.isRunning || lane.pausedElapsedTime > 0)) {
                        recordLap(lane)
                    } else lane
                }
            )
        }
    }

    private fun recordLap(lane: Lane): Lane {
        val currentTime = lane.currentElapsedTime
        val lastLapTime = lane.lastLapTime
        val lapDuration = currentTime - lastLapTime

        if (lapDuration < 100) return lane // Debounce

        val newLap = LapTime(
            lapNumber = lane.lapCount + 1,
            elapsedTime = currentTime,
            lapDuration = lapDuration
        )

        return lane.copy(laps = lane.laps + newLap)
    }

    @OptIn(ExperimentalTime::class)
    private fun startLane(laneId: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (lane.id == laneId && !lane.isRunning) {
                        lane.copy(isRunning = true, startTime = now)
                    } else lane
                },
                raceState = if (state.raceState == RaceState.IDLE) RaceState.RUNNING else state.raceState
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun stopLane(laneId: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (lane.id == laneId && lane.isRunning && lane.startTime != null) {
                        val elapsed = lane.pausedElapsedTime + (now - lane.startTime)
                        lane.copy(isRunning = false, startTime = null, pausedElapsedTime = elapsed)
                    } else lane
                }
            )
        }
    }

    private fun resetLane(laneId: String) {
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    if (lane.id == laneId) {
                        lane.copy(isRunning = false, startTime = null, pausedElapsedTime = 0L, laps = emptyList())
                    } else lane
                }
            )
        }
    }

    private fun resetAllLanes() {
        _state.update { state ->
            state.copy(
                lanes = state.lanes.map { lane ->
                    lane.copy(isRunning = false, startTime = null, pausedElapsedTime = 0L, laps = emptyList())
                },
                raceState = RaceState.IDLE
            )
        }
    }

    private fun resetEverything() {
        countdownJob?.cancel()
        restTimerJob?.cancel()
        _state.update {
            RaceTimerState(
                theme = it.theme,
                colorPalette = it.colorPalette,
                presets = it.presets
            )
        }
    }

    private fun startCountdown(seconds: Int) {
        countdownJob?.cancel()
        _state.update {
            it.copy(
                countdownSeconds = seconds,
                countdownRemaining = seconds,
                raceState = RaceState.COUNTDOWN,
                showCountdownDialog = false
            )
        }

        countdownJob = viewModelScope.launch {
            for (i in seconds downTo 1) {
                _state.update { it.copy(countdownRemaining = i) }
                delay(1000)
            }
            _state.update { it.copy(countdownRemaining = 0) }
            delay(100)
            startAll()
        }
    }

    private fun startRestTimer(seconds: Int) {
        restTimerJob?.cancel()
        _state.update {
            it.copy(
                restTimerSeconds = seconds,
                restTimerRemaining = seconds,
                isRestTimerRunning = true,
                showRestTimerDialog = false
            )
        }

        restTimerJob = viewModelScope.launch {
            for (i in seconds downTo 1) {
                _state.update { it.copy(restTimerRemaining = i) }
                delay(1000)
            }
            _state.update { it.copy(restTimerRemaining = 0, isRestTimerRunning = false) }
        }
    }

    private fun stopRestTimer() {
        restTimerJob?.cancel()
        _state.update { it.copy(isRestTimerRunning = false, restTimerRemaining = 0) }
    }

    private fun addCustomRestPreset(seconds: Int) {
        if (seconds <= 0) return
        _state.update { state ->
            if (seconds in state.customRestPresets) {
                state // Already exists
            } else {
                state.copy(customRestPresets = (state.customRestPresets + seconds).sorted())
            }
        }
    }

    private fun removeCustomRestPreset(seconds: Int) {
        _state.update { it.copy(customRestPresets = it.customRestPresets.filter { s -> s != seconds }) }
    }

    private fun savePreset(name: String) {
        val currentLanes = _state.value.lanes
        if (currentLanes.isEmpty() || name.isBlank()) return

        val preset = TimerPreset(
            id = IdGenerator.generatePresetId(),
            name = name.trim(),
            laneNames = currentLanes.map { it.name },
            laneColors = currentLanes.map { it.color }
        )

        _state.update { it.copy(presets = it.presets + preset, showSavePresetDialog = false) }
    }

    private fun loadPreset(presetId: String) {
        val preset = _state.value.presets.find { it.id == presetId } ?: return

        val newLanes = preset.laneNames.mapIndexed { index, name ->
            Lane(
                id = IdGenerator.generateId(),
                name = name,
                position = index + 1,
                color = preset.laneColors.getOrElse(index) { LaneColor.DEFAULT }
            )
        }

        _state.update { it.copy(lanes = newLanes, raceState = RaceState.IDLE, showLoadPresetDialog = false) }
    }

    private fun deletePreset(presetId: String) {
        _state.update { it.copy(presets = it.presets.filter { preset -> preset.id != presetId }) }
    }

    private fun setTheme(theme: AppTheme) {
        _state.update { it.copy(theme = theme, showThemeSelector = false) }
    }

    private fun setColorPalette(palette: String) {
        _state.update { it.copy(colorPalette = palette) }
    }

    private fun expandLane(laneId: String?) {
        _state.update { it.copy(expandedLaneId = laneId) }
    }

    private fun showDialog(dialog: DialogType) {
        _state.update { state ->
            when (dialog) {
                DialogType.THEME_SELECTOR -> state.copy(showThemeSelector = true, showMenuDropdown = false)
                DialogType.COUNTDOWN -> state.copy(showCountdownDialog = true, showMenuDropdown = false)
                DialogType.REST_TIMER -> state.copy(showRestTimerDialog = true, showMenuDropdown = false)
                DialogType.SAVE_PRESET -> state.copy(showSavePresetDialog = true, showMenuDropdown = false)
                DialogType.LOAD_PRESET -> state.copy(showLoadPresetDialog = true, showMenuDropdown = false)
                DialogType.SHARE -> state.copy(showShareDialog = true, showMenuDropdown = false)
                DialogType.ADD_PACE -> state.copy(showAddPaceDialog = true, showMenuDropdown = false)
                DialogType.ADD_NOTE -> state.copy(showAddNoteDialog = true, showMenuDropdown = false)
                DialogType.MANAGE_MARKERS -> state.copy(showManageMarkersDialog = true, showMenuDropdown = false)
            }
        }
    }

    private fun showRenameLaneDialog(laneId: String) {
        _state.update { it.copy(showRenameLaneDialog = laneId) }
    }

    private fun dismissDialog() {
        _state.update {
            it.copy(
                showThemeSelector = false,
                showMenuDropdown = false,
                showCountdownDialog = false,
                showRestTimerDialog = false,
                showSavePresetDialog = false,
                showLoadPresetDialog = false,
                showShareDialog = false,
                showRenameLaneDialog = null,
                showAddPaceDialog = false,
                showAddNoteDialog = false,
                showManageMarkersDialog = false
            )
        }
    }

    // Pace marker functions
    private fun addPaceMarker(event: RaceEvent, splitInterval: SplitInterval, targetTime: Long) {
        val newMarker = PaceMarker(
            id = IdGenerator.generatePaceId(),
            event = event,
            splitInterval = splitInterval,
            targetTime = targetTime
        )
        _state.update {
            it.copy(
                paceMarkers = it.paceMarkers + newMarker,
                showAddPaceDialog = false
            )
        }
    }

    private fun removePaceMarker(markerId: String) {
        _state.update { it.copy(paceMarkers = it.paceMarkers.filter { m -> m.id != markerId }) }
    }

    private fun togglePaceMarkerVisibility(markerId: String) {
        _state.update { state ->
            state.copy(
                paceMarkers = state.paceMarkers.map { marker ->
                    if (marker.id == markerId) marker.copy(isVisible = !marker.isVisible)
                    else marker
                }
            )
        }
    }

    // Coach note functions
    private fun addCoachNote(text: String) {
        if (text.isBlank()) return
        val newNote = CoachNote(
            id = IdGenerator.generateNoteId(),
            text = text.trim()
        )
        _state.update {
            it.copy(
                coachNotes = it.coachNotes + newNote,
                showAddNoteDialog = false
            )
        }
    }

    private fun removeCoachNote(noteId: String) {
        _state.update { it.copy(coachNotes = it.coachNotes.filter { n -> n.id != noteId }) }
    }

    private fun updateCoachNote(noteId: String, text: String) {
        _state.update { state ->
            state.copy(
                coachNotes = state.coachNotes.map { note ->
                    if (note.id == noteId) note.copy(text = text.trim())
                    else note
                }
            )
        }
    }

    private fun toggleNoteVisibility(noteId: String) {
        _state.update { state ->
            state.copy(
                coachNotes = state.coachNotes.map { note ->
                    if (note.id == noteId) note.copy(isVisible = !note.isVisible)
                    else note
                }
            )
        }
    }

    private fun toggleMenuDropdown() {
        _state.update { it.copy(showMenuDropdown = !it.showMenuDropdown) }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        countdownJob?.cancel()
        restTimerJob?.cancel()
    }
}
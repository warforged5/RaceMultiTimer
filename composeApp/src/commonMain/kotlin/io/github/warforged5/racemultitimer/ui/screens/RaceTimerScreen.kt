package com.racetimer.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.racetimer.app.ui.components.*
import io.github.warforged5.racemultitimer.domain.RaceState
import io.github.warforged5.racemultitimer.domain.TimeUtils
import io.github.warforged5.racemultitimer.ui.components.MarkersSection
import io.github.warforged5.racemultitimer.viewmodel.DialogType
import io.github.warforged5.racemultitimer.viewmodel.RaceTimerEvent
import io.github.warforged5.racemultitimer.viewmodel.RaceTimerState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaceTimerScreen(
    state: RaceTimerState,
    onEvent: (RaceTimerEvent) -> Unit
) {
    // Handle expanded lane view
    state.expandedLaneId?.let { laneId ->
        val lane = state.lanes.find { it.id == laneId }
        if (lane != null) {
            ExpandedLaneScreen(
                lane = lane,
                currentTimeMillis = state.currentTimeMillis,
                onBack = { onEvent(RaceTimerEvent.ExpandLane(null)) },
                onLap = { onEvent(RaceTimerEvent.LapLane(laneId)) },
                onStart = { onEvent(RaceTimerEvent.StartLane(laneId)) },
                onStop = { onEvent(RaceTimerEvent.StopLane(laneId)) },
                onReset = { onEvent(RaceTimerEvent.ResetLane(laneId)) },
                onRename = { onEvent(RaceTimerEvent.ShowRenameLaneDialog(laneId)) }
            )

            // Show rename dialog even in expanded view
            state.showRenameLaneDialog?.let { renameLaneId ->
                val renameLane = state.lanes.find { it.id == renameLaneId }
                if (renameLane != null) {
                    RenameLaneDialog(
                        currentName = renameLane.name,
                        onRename = { newName -> onEvent(RaceTimerEvent.RenameLane(renameLaneId, newName)) },
                        onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
                    )
                }
            }
            return
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Race Stopwatch",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = { onEvent(RaceTimerEvent.ShowDialog(DialogType.THEME_SELECTOR)) }) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Box {
                        IconButton(onClick = { onEvent(RaceTimerEvent.ToggleMenuDropdown) }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                        }

                        DropdownMenu(
                            expanded = state.showMenuDropdown,
                            onDismissRequest = { onEvent(RaceTimerEvent.ToggleMenuDropdown) }
                        ) {
                            // Show Laps toggle
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Show Laps ")
                                        Switch(
                                            checked = state.showLapsInCards,
                                            onCheckedChange = null,
                                            thumbContent = if (state.showLapsInCards) {
                                                {
                                                    Icon(
                                                        imageVector = Icons.Filled.Check,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                                    )
                                                }
                                            } else {
                                                null
                                            }

                                        )
                                    }
                                },
                                onClick = { onEvent(RaceTimerEvent.ToggleShowLaps) },
                                leadingIcon = { Icon(Icons.Outlined.FormatListNumbered, null) }
                            )

                            HorizontalDivider()

                            DropdownMenuItem(
                                text = { Text("Reset All Lanes") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ResetAllLanes)
                                },
                                leadingIcon = { Icon(Icons.Outlined.Refresh, null) },
                                enabled = state.lanes.isNotEmpty()
                            )

                            DropdownMenuItem(
                                text = { Text("Reset Everything") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ResetEverything)
                                },
                                leadingIcon = { Icon(Icons.Outlined.DeleteSweep, null) }
                            )

                            HorizontalDivider()

                            DropdownMenuItem(
                                text = { Text("Start Countdown") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.COUNTDOWN))
                                },
                                leadingIcon = { Icon(Icons.Outlined.Schedule, null) }
                            )

                            DropdownMenuItem(
                                text = { Text("Rest Timer") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.REST_TIMER))
                                },
                                leadingIcon = { Icon(Icons.Outlined.Timelapse, null) }
                            )

                            HorizontalDivider()

                            DropdownMenuItem(
                                text = { Text("Save Preset") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.SAVE_PRESET))
                                },
                                leadingIcon = { Icon(Icons.Outlined.Save, null) },
                                enabled = state.lanes.isNotEmpty()
                            )

                            DropdownMenuItem(
                                text = { Text("Load Preset") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.LOAD_PRESET))
                                },
                                leadingIcon = { Icon(Icons.Outlined.FolderOpen, null) }
                            )

                            HorizontalDivider()

                            DropdownMenuItem(
                                text = { Text("Share Results") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.SHARE))
                                },
                                leadingIcon = { Icon(Icons.Outlined.Share, null) },
                                enabled = state.lanes.isNotEmpty()
                            )

                            HorizontalDivider()

                            DropdownMenuItem(
                                text = { Text("Add Expected Pace") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.ADD_PACE))
                                },
                                leadingIcon = { Icon(Icons.Outlined.Speed, null) }
                            )

                            DropdownMenuItem(
                                text = { Text("Add Note") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.ADD_NOTE))
                                },
                                leadingIcon = { Icon(Icons.Outlined.StickyNote2, null) }
                            )

                            DropdownMenuItem(
                                text = { Text("Manage Markers") },
                                onClick = {
                                    onEvent(RaceTimerEvent.ToggleMenuDropdown)
                                    onEvent(RaceTimerEvent.ShowDialog(DialogType.MANAGE_MARKERS))
                                },
                                leadingIcon = { Icon(Icons.Outlined.Tune, null) },
                                enabled = state.paceMarkers.isNotEmpty() || state.coachNotes.isNotEmpty()
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Pace markers and notes section
                MarkersSection(
                    paceMarkers = state.paceMarkers,
                    coachNotes = state.coachNotes,
                    onRemovePaceMarker = { onEvent(RaceTimerEvent.RemovePaceMarker(it)) },
                    onRemoveNote = { onEvent(RaceTimerEvent.RemoveCoachNote(it)) },
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Rest timer bar
                AnimatedVisibility(
                    visible = state.isRestTimerRunning,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    RestTimerBar(
                        remainingSeconds = state.restTimerRemaining,
                        totalSeconds = state.restTimerSeconds,
                        onStop = { onEvent(RaceTimerEvent.StopRestTimer) },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Main content
                if (state.lanes.isEmpty()) {
                    EmptyState(modifier = Modifier.weight(1f).fillMaxWidth())
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(
                            start = 16.dp, end = 16.dp, top = 8.dp, bottom = 100.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(state.lanes, key = { _, lane -> lane.id }) { _, lane ->
                            LaneCard(
                                lane = lane,
                                currentTimeMillis = state.currentTimeMillis,
                                showLaps = state.showLapsInCards,
                                onLap = { onEvent(RaceTimerEvent.LapLane(lane.id)) },
                                onStartStop = {
                                    if (lane.isRunning) {
                                        onEvent(RaceTimerEvent.StopLane(lane.id))
                                    } else {
                                        onEvent(RaceTimerEvent.StartLane(lane.id))
                                    }
                                },
                                onInfo = { onEvent(RaceTimerEvent.ExpandLane(lane.id)) },
                                onDelete = { onEvent(RaceTimerEvent.RemoveLane(lane.id)) },
                                onEdit = { onEvent(RaceTimerEvent.ShowRenameLaneDialog(lane.id)) },
                                onReset = { onEvent(RaceTimerEvent.ResetLane(lane.id)) },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }

            // Floating toolbar
            FloatingToolbar(
                raceState = state.raceState,
                hasLanes = state.lanes.isNotEmpty(),
                onAddLane = { onEvent(RaceTimerEvent.AddLane) },
                onStartAll = { onEvent(RaceTimerEvent.StartAll) },
                onStopAll = { onEvent(RaceTimerEvent.StopAll) },
                onLapAll = { onEvent(RaceTimerEvent.LapAll) },
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
            )

            // Countdown overlay
            AnimatedVisibility(
                visible = state.raceState == RaceState.COUNTDOWN,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CountdownOverlay(seconds = state.countdownRemaining)
            }
        }
    }

    // Dialogs
    if (state.showThemeSelector) {
        ThemeSelectorDialog(
            currentTheme = state.theme,
            currentPalette = state.colorPalette,
            onThemeSelected = { onEvent(RaceTimerEvent.SetTheme(it)) },
            onPaletteSelected = { onEvent(RaceTimerEvent.SetColorPalette(it)) },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    if (state.showCountdownDialog) {
        CountdownDialog(
            initialSeconds = state.countdownSeconds,
            onStart = { onEvent(RaceTimerEvent.StartCountdown(it)) },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    if (state.showRestTimerDialog) {
        RestTimerDialog(
            initialSeconds = state.restTimerSeconds,
            customPresets = state.customRestPresets,
            onStart = { onEvent(RaceTimerEvent.StartRestTimer(it)) },
            onAddPreset = { onEvent(RaceTimerEvent.AddCustomRestPreset(it)) },
            onRemovePreset = { onEvent(RaceTimerEvent.RemoveCustomRestPreset(it)) },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    if (state.showSavePresetDialog) {
        SavePresetDialog(
            onSave = { onEvent(RaceTimerEvent.SavePreset(it)) },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    if (state.showLoadPresetDialog) {
        LoadPresetDialog(
            presets = state.presets,
            onLoad = { onEvent(RaceTimerEvent.LoadPreset(it)) },
            onDelete = { onEvent(RaceTimerEvent.DeletePreset(it)) },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    if (state.showShareDialog) {
        val shareableLanes = state.lanes.map { lane ->
            val elapsed = if (lane.isRunning && lane.startTime != null) {
                lane.pausedElapsedTime + (state.currentTimeMillis - lane.startTime)
            } else {
                lane.pausedElapsedTime
            }
            ShareableLane(
                id = lane.id,
                name = lane.name,
                totalTime = elapsed,
                timeFormatted = TimeUtils.formatTime(elapsed),
                lapCount = lane.laps.size,
                laps = lane.laps.map { lap ->
                    ShareableLap(
                        number = lap.lapNumber,
                        duration = TimeUtils.formatTime(lap.lapDuration)
                    )
                },
                averageLap = if (lane.laps.isNotEmpty()) {
                    TimeUtils.formatLapTime(lane.laps.map { it.lapDuration }.average().toLong())
                } else "",
                bestLap = lane.laps.minByOrNull { it.lapDuration }?.let {
                    TimeUtils.formatLapTime(it.lapDuration)
                }
            )
        }

        ShareResultsDialog(
            lanes = shareableLanes,
            onCopy = { /* Platform copy */ },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    // Rename lane dialog
    state.showRenameLaneDialog?.let { laneId ->
        val lane = state.lanes.find { it.id == laneId }
        if (lane != null) {
            RenameLaneDialog(
                currentName = lane.name,
                onRename = { newName -> onEvent(RaceTimerEvent.RenameLane(laneId, newName)) },
                onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
            )
        }
    }

    // Add pace dialog
    if (state.showAddPaceDialog) {
        AddPaceDialog(
            onAdd = { event, split, time ->
                onEvent(RaceTimerEvent.AddPaceMarker(event, split, time))
            },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    // Add note dialog
    if (state.showAddNoteDialog) {
        AddNoteDialog(
            onAdd = { text -> onEvent(RaceTimerEvent.AddCoachNote(text)) },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }

    // Manage markers dialog
    if (state.showManageMarkersDialog) {
        ManageMarkersDialog(
            paceMarkers = state.paceMarkers,
            coachNotes = state.coachNotes,
            onTogglePaceVisibility = { onEvent(RaceTimerEvent.TogglePaceMarkerVisibility(it)) },
            onRemovePace = { onEvent(RaceTimerEvent.RemovePaceMarker(it)) },
            onToggleNoteVisibility = { onEvent(RaceTimerEvent.ToggleNoteVisibility(it)) },
            onRemoveNote = { onEvent(RaceTimerEvent.RemoveCoachNote(it)) },
            onDismiss = { onEvent(RaceTimerEvent.DismissDialog) }
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Timer,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
            )

            Text(
                text = "No lanes yet",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Tap + to add a lane",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

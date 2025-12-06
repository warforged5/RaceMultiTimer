package com.racetimer.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.racetimer.app.ui.theme.AppThemeExtras
import io.github.warforged5.racemultitimer.domain.Lane
import io.github.warforged5.racemultitimer.domain.LapTime
import io.github.warforged5.racemultitimer.domain.TimeUtils


/**
 * Full-screen lane detail view with continuous timer updates
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedLaneScreen(
    lane: Lane,
    currentTimeMillis: Long,
    onBack: () -> Unit,
    onLap: () -> Unit,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit,
    onRename: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    // Calculate live elapsed time
    val elapsedTime = remember(currentTimeMillis, lane.isRunning, lane.startTime, lane.pausedElapsedTime) {
        if (lane.isRunning && lane.startTime != null) {
            lane.pausedElapsedTime + (currentTimeMillis - lane.startTime)
        } else {
            lane.pausedElapsedTime
        }
    }

    val lastLapTime = lane.laps.lastOrNull()?.elapsedTime ?: 0L
    val currentLapDuration = elapsedTime - lastLapTime

    // Calculate stats based on current elapsed time
    val lapCount = lane.laps.size
    val averageLapTime = if (lane.laps.isNotEmpty()) {
        lane.laps.map { it.lapDuration }.average().toLong()
    } else 0L
    val bestLapTime = lane.laps.minByOrNull { it.lapDuration }?.lapDuration
    val worstLapTime = lane.laps.maxByOrNull { it.lapDuration }?.lapDuration

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = lane.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onRename) {
                        Icon(Icons.Outlined.Edit, "Rename", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Main timer display
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = TimeUtils.formatTime(elapsedTime),
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Default,
                        fontSize = 56.sp
                    ),
                    color = if (lane.isRunning) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Lap: ${TimeUtils.formatTime(currentLapDuration)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (lane.isRunning) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Text(
                            text = "RUNNING",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Stats row
            if (lapCount > 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("Laps", lapCount.toString())
                    StatItem("Average", TimeUtils.formatLapTime(averageLapTime))
                    bestLapTime?.let { StatItem("Best", TimeUtils.formatLapTime(it)) }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Control buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Start/Stop
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        if (lane.isRunning) onStop() else onStart()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (lane.isRunning) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        },
                        contentColor = if (lane.isRunning) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    )
                ) {
                    Icon(
                        if (lane.isRunning) Icons.Outlined.Stop else Icons.Outlined.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (lane.isRunning) "Stop" else "Start",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Lap
                OutlinedButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLap()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = lane.isRunning || elapsedTime > 0
                ) {
                    Icon(Icons.Outlined.Flag, contentDescription = null, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Lap", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Reset button
            TextButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onReset()
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Outlined.Refresh, null, Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Reset")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lap times list
            if (lane.laps.isNotEmpty()) {
                Text(
                    text = "Lap Times",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(lane.laps.reversed()) { _, lap ->
                        LapTimeRow(
                            lap = lap,
                            isBest = lap.lapDuration == bestLapTime,
                            isWorst = lap.lapDuration == worstLapTime && lapCount > 2
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.Timer,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No lap times yet",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun LapTimeRow(
    lap: LapTime,
    isBest: Boolean,
    isWorst: Boolean
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = when {
            isBest -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
            isWorst -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f)
            else -> MaterialTheme.colorScheme.surfaceContainer
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${lap.lapNumber}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(32.dp)
                )

                Text(
                    text = TimeUtils.formatTime(lap.lapDuration),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            if (isBest) {
                Text(
                    text = "BEST",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
            } else if (isWorst) {
                Text(
                    text = "SLOW",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
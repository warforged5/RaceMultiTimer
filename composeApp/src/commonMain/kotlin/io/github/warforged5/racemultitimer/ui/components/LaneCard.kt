package com.racetimer.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.racetimer.app.ui.theme.AppThemeExtras
import io.github.warforged5.racemultitimer.domain.Lane
import io.github.warforged5.racemultitimer.domain.TimeUtils
import androidx.compose.foundation.interaction.MutableInteractionSource

/**
 * Clean, flat lane card
 * Tap anywhere on card (except buttons) to lap
 */
@Composable
fun LaneCard(
    lane: Lane,
    currentTimeMillis: Long, // For continuous updates
    showLaps: Boolean = true, // Toggle for showing laps
    onLap: () -> Unit,
    onStartStop: () -> Unit,
    onInfo: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val iconTint = MaterialTheme.colorScheme.primary

    // Calculate current elapsed time using the provided timestamp
    val elapsedTime = remember(currentTimeMillis, lane.isRunning, lane.startTime, lane.pausedElapsedTime) {
        if (lane.isRunning && lane.startTime != null) {
            lane.pausedElapsedTime + (currentTimeMillis - lane.startTime)
        } else {
            lane.pausedElapsedTime
        }
    }

    val lastLapTime = lane.laps.lastOrNull()?.elapsedTime ?: 0L
    val currentLapDuration = elapsedTime - lastLapTime

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onLap()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top row: Lane name + action icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = lane.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                // Action icons - stop click propagation
                Row(
                    horizontalArrangement = Arrangement.spacedBy(0.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onInfo()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Details",
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDelete()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete",
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onEdit()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Rename",
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onReset()
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Reset",
                            tint = iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom row: Timer display + play/stop button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Main time - using memoized value
                    Text(
                        text = TimeUtils.formatTime(elapsedTime),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp
                        ),
                        color = if (lane.isRunning) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )

                    Text(
                        text = " | ",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Lap: ",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = TimeUtils.formatTime(currentLapDuration),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = FontFamily.Default,
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Play/Stop button
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onStartStop()
                    },
                    modifier = Modifier.size(44.dp)
                ) {
                    Icon(
                        imageVector = if (lane.isRunning) {
                            Icons.Outlined.Stop
                        } else {
                            Icons.Outlined.PlayArrow
                        },
                        contentDescription = if (lane.isRunning) "Stop" else "Start",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Lap list section
            AnimatedVisibility(
                visible = showLaps && lane.laps.isNotEmpty(),
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Lap times in a compact grid-like layout
                    lane.laps.reversed().chunked(3).forEach { rowLaps ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowLaps.forEach { lap ->
                                LapChip(
                                    lapNumber = lap.lapNumber,
                                    duration = lap.lapDuration,
                                    isBest = lane.bestLapTime == lap.lapDuration && lane.laps.size > 1,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            // Fill remaining space if less than 3 laps in row
                            repeat(3 - rowLaps.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact lap chip showing lap number and time
 */
@Composable
private fun LapChip(
    lapNumber: Int,
    duration: Long,
    isBest: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = if (isBest) {
            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "$lapNumber.",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = TimeUtils.formatLapTime(duration),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontFamily = FontFamily.Default
                ),
                color = if (isBest) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (isBest) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

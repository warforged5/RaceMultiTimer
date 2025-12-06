package com.racetimer.app.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.racetimer.app.ui.theme.AppThemeExtras
import io.github.warforged5.racemultitimer.domain.RaceState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.scale

/**
 * Material 3 Expressive Horizontal Floating Toolbar
 *
 * Features:
 * - Pill-shaped container with elevated surface
 * - Icon-only buttons with spring animations
 * - FAB-style add button integrated
 * - Vibrant colors from theme
 */
@Composable
fun FloatingToolbar(
    raceState: RaceState,
    hasLanes: Boolean,
    onAddLane: () -> Unit,
    onStartAll: () -> Unit,
    onStopAll: () -> Unit,
    onLapAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRunning = raceState == RaceState.RUNNING

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Main toolbar container - M3 Expressive style
        Surface(
            modifier = Modifier
                .shadow(
                    elevation = 12.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ),
            shape = RoundedCornerShape(32.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            tonalElevation = 3.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add Lane - Primary FAB style
                ExpressiveToolbarFab(
                    icon = Icons.Outlined.Add,
                    onClick = onAddLane,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Start All
                ExpressiveToolbarButton(
                    icon = Icons.Outlined.PlayArrow,
                    onClick = onStartAll,
                    enabled = hasLanes && !isRunning
                )

                // Stop All
                ExpressiveToolbarButton(
                    icon = Icons.Outlined.Stop,
                    onClick = onStopAll,
                    enabled = hasLanes && isRunning
                )

                // Lap All
                ExpressiveToolbarButton(
                    icon = Icons.Outlined.Flag,
                    onClick = onLapAll,
                    enabled = hasLanes && (isRunning || raceState == RaceState.PAUSED)
                )
            }
        }
    }
}

/**
 * FAB-style button for primary action (Add)
 */
@Composable
private fun ExpressiveToolbarFab(
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "fabScale"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .size(56.dp)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Add Lane",
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

/**
 * Standard toolbar button with spring animation
 */
@Composable
private fun ExpressiveToolbarButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            isPressed -> 0.85f
            else -> 1f
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "buttonScale"
    )

    val contentColor = if (enabled) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    }

    Box(
        modifier = Modifier
            .scale(scale)
            .size(56.dp)
            .clip(CircleShape)
            .then(
                if (enabled) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onClick()
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(28.dp)
        )
    }
}

/**
 * Countdown overlay - clean and simple
 */
@Composable
fun CountdownOverlay(
    seconds: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        val scale by animateFloatAsState(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "countdownScale"
        )

        Text(
            text = if (seconds > 0) seconds.toString() else "GO!",
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = when {
                seconds == 0 -> MaterialTheme.colorScheme.tertiary
                seconds == 1 -> MaterialTheme.colorScheme.error
                else -> Color.White
            },
            modifier = Modifier.scale(scale)
        )
    }
}

/**
 * Rest timer bar - keeping same design
 */
@Composable
fun RestTimerBar(
    remainingSeconds: Int,
    totalSeconds: Int,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = remainingSeconds.toFloat() / totalSeconds.toFloat()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Timer,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Rest",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = formatRestTime(remainingSeconds),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = MaterialTheme.colorScheme.tertiary,
                    trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onStop,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Stop",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun formatRestTime(seconds: Int): String {
    val min = seconds / 60
    val sec = seconds % 60
    return if (min > 0) {
        "${min}:${sec.toString().padStart(2, '0')}"
    } else {
        "${sec}s"
    }
}
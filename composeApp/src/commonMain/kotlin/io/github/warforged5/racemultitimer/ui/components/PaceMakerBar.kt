package io.github.warforged5.racemultitimer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.StickyNote2
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.warforged5.racemultitimer.domain.CoachNote
import io.github.warforged5.racemultitimer.domain.PaceMarker

/**
 * Thin bar displaying pace splits for an event
 */
@Composable
fun PaceMarkerBar(
    marker: PaceMarker,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val splits = remember(marker) { marker.getSplits() }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Scrollable splits
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                splits.forEachIndexed { index, split ->
                    // Split column
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        // Label (distance marker)
                        Text(
                            text = split.label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (split.isFinal) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            fontWeight = if (split.isFinal) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 10.sp
                        )

                        // Time
                        Text(
                            text = formatPaceTime(split.time),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Default,
                                fontWeight = if (split.isFinal) FontWeight.SemiBold else FontWeight.Normal,
                                fontSize = 11.sp
                            ),
                            color = if (split.isFinal) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }

                    // Separator (except after last)
                    if (index < splits.size - 1) {
                        Text(
                            text = "|",
                            color = MaterialTheme.colorScheme.outlineVariant,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 2.dp)
                        )
                    }
                }
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Remove",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Thin bar for coach notes
 */
@Composable
fun CoachNoteBar(
    note: CoachNote,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.StickyNote2,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = note.text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Remove",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * Container for all pace markers and notes
 */
@Composable
fun MarkersSection(
    paceMarkers: List<PaceMarker>,
    coachNotes: List<CoachNote>,
    onRemovePaceMarker: (String) -> Unit,
    onRemoveNote: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val visibleMarkers = paceMarkers.filter { it.isVisible }
    val visibleNotes = coachNotes.filter { it.isVisible }

    if (visibleMarkers.isEmpty() && visibleNotes.isEmpty()) return

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Notes first
        visibleNotes.forEach { note ->
            CoachNoteBar(
                note = note,
                onDelete = { onRemoveNote(note.id) }
            )
        }

        // Then pace markers
        visibleMarkers.forEach { marker ->
            PaceMarkerBar(
                marker = marker,
                onDelete = { onRemovePaceMarker(marker.id) }
            )
        }
    }
}

/**
 * Format time for pace display (no centiseconds for readability)
 */
private fun formatPaceTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60

    return if (minutes > 0) {
        "$minutes:${seconds.toString().padStart(2, '0')}"
    } else {
        "${seconds}s"
    }
}
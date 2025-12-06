package com.racetimer.app.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.warforged5.racemultitimer.domain.AppTheme
import io.github.warforged5.racemultitimer.domain.LaneColor
import io.github.warforged5.racemultitimer.domain.TimerPreset
import io.github.warforged5.racemultitimer.isAndroid
import androidx.compose.ui.text.style.TextOverflow
import io.github.warforged5.racemultitimer.domain.CoachNote
import io.github.warforged5.racemultitimer.domain.PaceMarker
import io.github.warforged5.racemultitimer.domain.RaceEvent
import io.github.warforged5.racemultitimer.domain.SplitInterval

/**
 * Theme and palette selector dialog
 */
@Composable
fun ThemeSelectorDialog(
    currentTheme: AppTheme,
    currentPalette: String,
    onThemeSelected: (AppTheme) -> Unit,
    onPaletteSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Appearance", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Theme mode section
                Text(
                    text = "Mode",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemeOption("System", Icons.Outlined.BrightnessAuto, currentTheme == AppTheme.SYSTEM) {
                        onThemeSelected(AppTheme.SYSTEM)
                    }
                    ThemeOption("Light", Icons.Outlined.LightMode, currentTheme == AppTheme.LIGHT) {
                        onThemeSelected(AppTheme.LIGHT)
                    }
                    ThemeOption("Dark", Icons.Outlined.DarkMode, currentTheme == AppTheme.DARK) {
                        onThemeSelected(AppTheme.DARK)
                    }
                    if (isAndroid()) {
                        ThemeOption("Dynamic", Icons.Outlined.Palette, currentTheme == AppTheme.DYNAMIC) {
                            onThemeSelected(AppTheme.DYNAMIC)
                        }
                    }
                }

                HorizontalDivider()

                // Color palette section
                Text(
                    text = "Color Palette",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PaletteOption(
                        name = "Indigo",
                        color = Color(0xFF5A5D95),
                        isSelected = currentPalette == "indigo",
                        onClick = { onPaletteSelected("indigo") }
                    )
                    PaletteOption(
                        name = "Teal",
                        color = Color(0xFF006A69),
                        isSelected = currentPalette == "teal",
                        onClick = { onPaletteSelected("teal") }
                    )
                    PaletteOption(
                        name = "Rose",
                        color = Color(0xFF9C4057),
                        isSelected = currentPalette == "rose",
                        onClick = { onPaletteSelected("rose") }
                    )
                    PaletteOption(
                        name = "Sage",
                        color = Color(0xFF4B6546),
                        isSelected = currentPalette == "sage",
                        onClick = { onPaletteSelected("sage") }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}

@Composable
private fun ThemeOption(
    name: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun PaletteOption(
    name: String,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (isSelected) {
                        Modifier.border(3.dp, MaterialTheme.colorScheme.outline, CircleShape)
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Countdown dialog
 */
@Composable
fun CountdownDialog(
    initialSeconds: Int,
    onStart: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var seconds by remember { mutableStateOf(initialSeconds) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Countdown", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilledIconButton(
                        onClick = { if (seconds > 1) seconds-- },
                        enabled = seconds > 1
                    ) {
                        Icon(Icons.Default.Remove, "Decrease")
                    }

                    Text(
                        text = "${seconds}s",
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.width(80.dp),
                        textAlign = TextAlign.Center
                    )

                    FilledIconButton(
                        onClick = { if (seconds < 30) seconds++ },
                        enabled = seconds < 30
                    ) {
                        Icon(Icons.Default.Add, "Increase")
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(3, 5, 10).forEach { preset ->
                        FilterChip(
                            selected = seconds == preset,
                            onClick = { seconds = preset },
                            label = { Text("${preset}s") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onStart(seconds) }) { Text("Start") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Rest timer dialog with custom preset support
 */
@Composable
fun RestTimerDialog(
    initialSeconds: Int,
    customPresets: List<Int> = emptyList(),
    onStart: (Int) -> Unit,
    onAddPreset: (Int) -> Unit,
    onRemovePreset: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var minutes by remember { mutableStateOf(initialSeconds / 60) }
    var seconds by remember { mutableStateOf(initialSeconds % 60) }

    val totalSeconds = minutes * 60 + seconds
    val defaultPresets = listOf(30, 60, 90, 120)
    val allPresets = (defaultPresets + customPresets).distinct().sorted()
    val isOnPreset = totalSeconds in allPresets
    val isCustomPreset = totalSeconds in customPresets
    val canAddPreset = totalSeconds > 0 && !isOnPreset

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Rest Timer", fontWeight = FontWeight.SemiBold)

                // Star button - shows for custom times or to remove custom presets
                if (canAddPreset || isCustomPreset) {
                    IconButton(
                        onClick = {
                            if (isCustomPreset) {
                                onRemovePreset(totalSeconds)
                            } else {
                                onAddPreset(totalSeconds)
                            }
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            if (isCustomPreset) Icons.Filled.Star else Icons.Outlined.StarOutline,
                            contentDescription = if (isCustomPreset) "Remove preset" else "Save as preset",
                            tint = if (isCustomPreset) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Minutes
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledIconButton(onClick = { if (minutes < 59) minutes++ }) {
                            Icon(Icons.Default.KeyboardArrowUp, "Increase")
                        }
                        Text(
                            text = minutes.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        FilledIconButton(
                            onClick = { if (minutes > 0) minutes-- },
                            enabled = minutes > 0
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, "Decrease")
                        }
                        Text("min", style = MaterialTheme.typography.labelSmall)
                    }

                    Text(":", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)

                    // Seconds
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledIconButton(onClick = { seconds = (seconds + 5) % 60 }) {
                            Icon(Icons.Default.KeyboardArrowUp, "Increase")
                        }
                        Text(
                            text = seconds.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        FilledIconButton(onClick = { seconds = if (seconds >= 5) seconds - 5 else 55 }) {
                            Icon(Icons.Default.KeyboardArrowDown, "Decrease")
                        }
                        Text("sec", style = MaterialTheme.typography.labelSmall)
                    }
                }

                // All presets in one row (scrollable)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    allPresets.forEach { value ->
                        FilterChip(
                            selected = totalSeconds == value,
                            onClick = {
                                minutes = value / 60
                                seconds = value % 60
                            },
                            label = { Text(formatPresetLabel(value)) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onStart(totalSeconds) },
                enabled = totalSeconds > 0
            ) { Text("Start") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

private fun formatPresetLabel(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return when {
        m == 0 -> "${s}s"
        s == 0 -> "${m}m"
        else -> "$m:${s.toString().padStart(2, '0')}"
    }
}

/**
 * Save preset dialog
 */
@Composable
fun SavePresetDialog(
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save Preset", fontWeight = FontWeight.SemiBold) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Preset name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (name.isNotBlank()) onSave(name) })
            )
        },
        confirmButton = {
            Button(onClick = { onSave(name) }, enabled = name.isNotBlank()) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Load preset dialog
 */
@Composable
fun LoadPresetDialog(
    presets: List<TimerPreset>,
    onLoad: (String) -> Unit,
    onDelete: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Load Preset", fontWeight = FontWeight.SemiBold) },
        text = {
            if (presets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.FolderOpen,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("No presets saved", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(presets) { preset ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onLoad(preset.id) },
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(preset.name, fontWeight = FontWeight.Medium)
                                    Text(
                                        "${preset.laneNames.size} lanes",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(onClick = { onDelete(preset.id) }) {
                                    Icon(
                                        Icons.Outlined.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Close") }
        }
    )
}

/**
 * Share results dialog - two step: selection then preview
 */
@Composable
fun ShareResultsDialog(
    lanes: List<ShareableLane>,
    onCopy: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var step by remember { mutableStateOf(ShareStep.SELECT) }
    var selectedLaneIds by remember { mutableStateOf(lanes.map { it.id }.toSet()) }
    var includeLapTimes by remember { mutableStateOf(true) }
    var generatedResults by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Share, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(
                    if (step == ShareStep.SELECT) "Export Results" else "Preview",
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            when (step) {
                ShareStep.SELECT -> {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // Select all / deselect all
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Select Lanes",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            TextButton(
                                onClick = {
                                    selectedLaneIds = if (selectedLaneIds.size == lanes.size) {
                                        emptySet()
                                    } else {
                                        lanes.map { it.id }.toSet()
                                    }
                                }
                            ) {
                                Text(
                                    if (selectedLaneIds.size == lanes.size) "Deselect All" else "Select All"
                                )
                            }
                        }

                        // Lane checkboxes
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 200.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(lanes) { lane ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedLaneIds = if (lane.id in selectedLaneIds) {
                                                selectedLaneIds - lane.id
                                            } else {
                                                selectedLaneIds + lane.id
                                            }
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (lane.id in selectedLaneIds) {
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceContainerHigh
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = lane.id in selectedLaneIds,
                                            onCheckedChange = { checked ->
                                                selectedLaneIds = if (checked) {
                                                    selectedLaneIds + lane.id
                                                } else {
                                                    selectedLaneIds - lane.id
                                                }
                                            }
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                lane.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                fontWeight = FontWeight.Medium
                                            )
                                            Text(
                                                lane.timeFormatted,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        if (lane.lapCount > 0) {
                                            Text(
                                                "${lane.lapCount} laps",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        HorizontalDivider()

                        // Include lap times toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { includeLapTimes = !includeLapTimes }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = includeLapTimes,
                                onCheckedChange = { includeLapTimes = it }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Include lap times", style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }

                ShareStep.PREVIEW -> {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Box(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Text(
                                text = generatedResults,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            when (step) {
                ShareStep.SELECT -> {
                    Button(
                        onClick = {
                            generatedResults = generateResults(
                                lanes.filter { it.id in selectedLaneIds },
                                includeLapTimes
                            )
                            step = ShareStep.PREVIEW
                        },
                        enabled = selectedLaneIds.isNotEmpty()
                    ) {
                        Text("Next")
                    }
                }
                ShareStep.PREVIEW -> {
                    Button(onClick = { onCopy(generatedResults) }) {
                        Icon(Icons.Outlined.ContentCopy, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Copy")
                    }
                }
            }
        },
        dismissButton = {
            when (step) {
                ShareStep.SELECT -> {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                }
                ShareStep.PREVIEW -> {
                    TextButton(onClick = { step = ShareStep.SELECT }) { Text("Back") }
                }
            }
        }
    )
}

private enum class ShareStep {
    SELECT, PREVIEW
}

/**
 * Data class for shareable lane info
 */
data class ShareableLane(
    val id: String,
    val name: String,
    val totalTime: Long,
    val timeFormatted: String,
    val lapCount: Int,
    val laps: List<ShareableLap>,
    val averageLap: String,
    val bestLap: String?
)

data class ShareableLap(
    val number: Int,
    val duration: String
)

private fun generateResults(lanes: List<ShareableLane>, includeLapTimes: Boolean): String {
    return buildString {
        appendLine("Race Results")
        appendLine("═".repeat(30))
        appendLine()

        lanes.forEachIndexed { index, lane ->
            appendLine("${lane.name}")
            appendLine("  Time: ${lane.timeFormatted}")

            if (lane.lapCount > 0) {
                appendLine("  Laps: ${lane.lapCount}")
                appendLine("  Average: ${lane.averageLap}")
                lane.bestLap?.let { appendLine("  Best: $it") }

                if (includeLapTimes && lane.laps.isNotEmpty()) {
                    appendLine()
                    appendLine("  Lap Times:")
                    lane.laps.forEach { lap ->
                        appendLine("    ${lap.number}. ${lap.duration}")
                    }
                }
            }

            if (index < lanes.size - 1) {
                appendLine()
            }
        }
    }
}

/**
 * Rename lane dialog
 */
@Composable
fun RenameLaneDialog(
    currentName: String,
    onRename: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Rename Lane", fontWeight = FontWeight.SemiBold) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Lane name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (name.isNotBlank()) onRename(name) })
            )
        },
        confirmButton = {
            Button(onClick = { onRename(name) }, enabled = name.isNotBlank()) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Color picker dialog
 */
@Composable
fun ColorPickerDialog(
    currentColor: LaneColor,
    onColorSelected: (LaneColor) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose Color", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LaneColor.entries.chunked(5).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(color.primary))
                                    .border(
                                        width = if (color == currentColor) 3.dp else 0.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = CircleShape
                                    )
                                    .clickable { onColorSelected(color) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (color == currentColor) {
                                    Icon(Icons.Default.Check, "Selected", tint = Color.White, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}

/**
 * Add pace marker dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaceDialog(
    onAdd: (RaceEvent, SplitInterval, Long) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedEvent by remember { mutableStateOf(RaceEvent.M800) }
    var selectedSplit by remember { mutableStateOf(SplitInterval.M400) }
    var minutes by remember { mutableStateOf(2) }
    var seconds by remember { mutableStateOf(0) }

    // Update default split when event changes
    LaunchedEffect(selectedEvent) {
        selectedSplit = when (selectedEvent) {
            RaceEvent.M100, RaceEvent.M200 -> SplitInterval.M100
            RaceEvent.M400 -> SplitInterval.M200
            RaceEvent.M800, RaceEvent.M1500, RaceEvent.M1600 -> SplitInterval.M400
            RaceEvent.M3000, RaceEvent.M3200 -> SplitInterval.M400
            RaceEvent.M5000, RaceEvent.M10000 -> SplitInterval.MILE
            RaceEvent.HALF_MARATHON, RaceEvent.MARATHON -> SplitInterval.MILE
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expected Pace", fontWeight = FontWeight.SemiBold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Event selector
                Text(
                    "Event",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // Event dropdown using ExposedDropdownMenuBox
                var eventExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = eventExpanded,
                    onExpandedChange = { eventExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedEvent.displayName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = eventExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = eventExpanded,
                        onDismissRequest = { eventExpanded = false }
                    ) {
                        RaceEvent.entries.forEach { event ->
                            DropdownMenuItem(
                                text = { Text(event.displayName) },
                                onClick = {
                                    selectedEvent = event
                                    eventExpanded = false
                                }
                            )
                        }
                    }
                }

                // Split interval selector
                Text(
                    "Show splits by",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    SplitInterval.entries.filter { split ->
                        split.distanceMeters < selectedEvent.distanceMeters
                    }.forEach { split ->
                        FilterChip(
                            selected = selectedSplit == split,
                            onClick = { selectedSplit = split },
                            label = { Text(split.displayName) }
                        )
                    }
                }

                // Target time input
                Text(
                    "Target Time",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Minutes
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledIconButton(
                            onClick = { if (minutes < 59) minutes++ },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, "Increase", Modifier.size(20.dp))
                        }
                        Text(
                            text = minutes.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        FilledIconButton(
                            onClick = { if (minutes > 0) minutes-- },
                            enabled = minutes > 0,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, "Decrease", Modifier.size(20.dp))
                        }
                    }

                    Text(
                        ":",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    // Seconds
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FilledIconButton(
                            onClick = { seconds = (seconds + 5) % 60 },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowUp, "Increase", Modifier.size(20.dp))
                        }
                        Text(
                            text = seconds.toString().padStart(2, '0'),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        FilledIconButton(
                            onClick = { seconds = if (seconds >= 5) seconds - 5 else 55 },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.KeyboardArrowDown, "Decrease", Modifier.size(20.dp))
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val totalMillis = ((minutes * 60) + seconds) * 1000L
                    if (totalMillis > 0) {
                        onAdd(selectedEvent, selectedSplit, totalMillis)
                    }
                },
                enabled = minutes > 0 || seconds > 0
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Add coach note dialog
 */
@Composable
fun AddNoteDialog(
    onAdd: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var noteText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Note", fontWeight = FontWeight.SemiBold) },
        text = {
            OutlinedTextField(
                value = noteText,
                onValueChange = { noteText = it },
                label = { Text("Note") },
                placeholder = { Text("e.g., Stay relaxed through 400m") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { if (noteText.isNotBlank()) onAdd(noteText) })
            )
        },
        confirmButton = {
            Button(
                onClick = { onAdd(noteText) },
                enabled = noteText.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Manage markers dialog - shows all markers and notes with visibility toggles
 */
@Composable
fun ManageMarkersDialog(
    paceMarkers: List<PaceMarker>,
    coachNotes: List<CoachNote>,
    onTogglePaceVisibility: (String) -> Unit,
    onRemovePace: (String) -> Unit,
    onToggleNoteVisibility: (String) -> Unit,
    onRemoveNote: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Manage Markers", fontWeight = FontWeight.SemiBold) },
        text = {
            if (paceMarkers.isEmpty() && coachNotes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Outlined.Timeline,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "No markers or notes",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.heightIn(max = 400.dp)
                ) {
                    // Notes section
                    if (coachNotes.isNotEmpty()) {
                        item {
                            Text(
                                "Notes",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        items(coachNotes) { note ->
                            MarkerListItem(
                                title = note.text,
                                subtitle = null,
                                isVisible = note.isVisible,
                                onToggleVisibility = { onToggleNoteVisibility(note.id) },
                                onDelete = { onRemoveNote(note.id) }
                            )
                        }
                    }

                    // Pace markers section
                    if (paceMarkers.isNotEmpty()) {
                        item {
                            if (coachNotes.isNotEmpty()) {
                                Spacer(Modifier.height(8.dp))
                            }
                            Text(
                                "Pace Markers",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        items(paceMarkers) { marker ->
                            val timeFormatted = formatMarkerTime(marker.targetTime)
                            MarkerListItem(
                                title = "${marker.event.displayName} - $timeFormatted",
                                subtitle = "by ${marker.splitInterval.displayName}",
                                isVisible = marker.isVisible,
                                onToggleVisibility = { onTogglePaceVisibility(marker.id) },
                                onDelete = { onRemovePace(marker.id) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Done") }
        }
    )
}

@Composable
private fun MarkerListItem(
    title: String,
    subtitle: String?,
    isVisible: Boolean,
    onToggleVisibility: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onToggleVisibility,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    if (isVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                    contentDescription = if (isVisible) "Hide" else "Show",
                    tint = if (isVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                subtitle?.let {
                    Text(
                        it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun formatMarkerTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "$minutes:${seconds.toString().padStart(2, '0')}"
}

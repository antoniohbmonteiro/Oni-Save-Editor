package br.com.antoniomonteiro.oni.saveeditor.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.antoniomonteiro.oni.saveeditor.ui.model.*
import br.com.antoniomonteiro.oni.saveeditor.ui.theme.DarkAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ColonySummaryScreen(modifier: Modifier = Modifier) {
    val gameSettings = remember { mutableStateListOf(*ColonySummaryMockData.initialGameSettings().toTypedArray()) }
    val booleanSettings = remember { mutableStateListOf(*ColonySummaryMockData.initialBooleanSettings().toTypedArray()) }
    var worldgenSeed by remember { mutableStateOf(ColonySummaryMockData.worldgenSeed) }
    var copiedSeed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LazyVerticalGrid(
        columns = GridCells.Fixed(1), // Main layout is a single column of cards
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Page Title
        item {
            Column {
                Text("Colony Summary", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                Text("Overview of your colony's metadata and settings", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Top Header: Meta Dados
        item { MetaDataCard() }

        // DLCs Card
        item { DlcCard() }

        // Cluster/World Info
        item { ClusterInfoCard() }

        // Game Settings / Difficulty
        item {
            GameSettingsCard(
                gameSettings = gameSettings,
                booleanSettings = booleanSettings,
                worldgenSeed = worldgenSeed,
                copiedSeed = copiedSeed,
                onSeedChange = { worldgenSeed = it },
                onCopySeed = {
                    coroutineScope.launch {
                        copiedSeed = true
                        delay(2000)
                        copiedSeed = false
                    }
                },
                onBooleanToggle = { index ->
                    booleanSettings[index] = booleanSettings[index].copy(enabled = !booleanSettings[index].enabled)
                },
                onSettingChange = { index, direction ->
                    val setting = gameSettings[index]
                    val optionsLength = setting.options.size
                    val newIndex = if (direction == "prev") {
                        (setting.currentIndex - 1 + optionsLength) % optionsLength
                    } else {
                        (setting.currentIndex + 1) % optionsLength
                    }
                    gameSettings[index] = setting.copy(currentIndex = newIndex)
                }
            )
        }
    }
}

@Composable
private fun MetaDataCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
        shape = MaterialTheme.shapes.large
    ) {
        val metaDataItems = listOf(
            Triple(Icons.Outlined.Description, "File", ColonySummaryMockData.fileName),
            Triple(Icons.Outlined.Home, "Base Name", ColonySummaryMockData.baseName),
            Triple(Icons.Outlined.CalendarToday, "Cycle", ColonySummaryMockData.cycle.toString()),
            Triple(Icons.Outlined.Groups, "Duplicants (Total)", ColonySummaryMockData.totalDuplicants.toString())
        )

        Row(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            metaDataItems.forEach { (icon, label, value) ->
                MetaDataItem(icon, label, value, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun MetaDataItem(icon: ImageVector, label: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), MaterialTheme.shapes.medium)
                .padding(8.dp)
        ) {
            Icon(icon, contentDescription = label, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
private fun DlcCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("DLCs", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp), color = MaterialTheme.colorScheme.onSurface)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ColonySummaryMockData.dlcs.chunked(2).forEach { rowItems ->
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        rowItems.forEach { dlc ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = dlc.name,
                                    color = if (dlc.enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                if (dlc.enabled) {
                                    Icon(Icons.Outlined.CheckCircle, contentDescription = "Enabled", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                } else {
                                    Icon(Icons.Outlined.Cancel, contentDescription = "Disabled", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                        if (rowItems.size < 2) {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ClusterInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Cluster / World Info", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(bottom = 16.dp), color = MaterialTheme.colorScheme.onSurface)
            
            Column(modifier = Modifier.padding(bottom = 16.dp)) {
                Text("Starting Asteroid / World", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = ColonySummaryMockData.startingAsteroid,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Column {
                Text("World Traits", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    ColonySummaryMockData.worldTraits.forEach { trait ->
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = trait,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameSettingsCard(
    gameSettings: List<GameSettingEnum>,
    booleanSettings: List<BooleanSetting>,
    worldgenSeed: String,
    copiedSeed: Boolean,
    onSeedChange: (String) -> Unit,
    onCopySeed: () -> Unit,
    onBooleanToggle: (Int) -> Unit,
    onSettingChange: (Int, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Text("Game Settings", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)

            // Worldgen Seed
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text("Worldgen Seed", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = worldgenSeed,
                        onValueChange = onSeedChange,
                        modifier = Modifier.weight(1f),
                        textStyle = LocalTextStyle.current.copy(fontFamily = FontFamily.Monospace),
                        shape = MaterialTheme.shapes.medium
                    )
                    OutlinedButton(onClick = onCopySeed, modifier = Modifier.size(48.dp), contentPadding = PaddingValues(0.dp), shape = MaterialTheme.shapes.medium) {
                        if (copiedSeed) {
                            Icon(Icons.Outlined.Check, contentDescription = "Copied", tint = MaterialTheme.colorScheme.primary)
                        } else {
                            Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy Seed", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            // Boolean Settings
            Column {
                Text("Feature Toggles", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 12.dp))
                // Replaced LazyVerticalGrid with a simple Column and Rows to fix the crash.
                // For a small, fixed number of items, this is safer than nested lazy layouts.
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    booleanSettings.chunked(2).forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            rowItems.forEach { setting ->
                                val index = booleanSettings.indexOf(setting)
                                SettingGridItem(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = setting.name,
                                            color = if (setting.enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Switch(
                                            checked = setting.enabled,
                                            onCheckedChange = { onBooleanToggle(index) },
                                            modifier = Modifier.scale(0.65f),
                                            colors = SwitchDefaults.colors(
                                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                                                uncheckedBorderColor = MaterialTheme.colorScheme.outline,
                                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                                checkedTrackColor = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    }
                                }
                            }
                            if (rowItems.size < 2) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // Difficulty Settings
            Column {
                Text("Difficulty Settings", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 12.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    gameSettings.chunked(2).forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            rowItems.forEach { setting ->
                                val index = gameSettings.indexOf(setting)
                                SettingGridItem(modifier = Modifier.weight(1f)) {
                                    Column {
                                        Text(setting.name, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(bottom = 12.dp))
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                            OutlinedButton(onClick = { onSettingChange(index, "prev") }, modifier = Modifier.size(36.dp), contentPadding = PaddingValues(0.dp)) {
                                                Icon(Icons.Outlined.ChevronLeft, contentDescription = "Previous", tint = MaterialTheme.colorScheme.primary)
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(36.dp)
                                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f), MaterialTheme.shapes.medium),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                val currentOption = setting.options[setting.currentIndex]
                                                val optionColor = when {
                                                    setting.currentIndex <= 1 -> MaterialTheme.colorScheme.primary // Easy
                                                    setting.currentIndex >= setting.options.size - 2 -> DarkAccent // Hard
                                                    else -> MaterialTheme.colorScheme.onSurface // Default
                                                }
                                                Text(currentOption, color = optionColor, style = MaterialTheme.typography.bodyMedium)
                                            }
                                            OutlinedButton(onClick = { onSettingChange(index, "next") }, modifier = Modifier.size(36.dp), contentPadding = PaddingValues(0.dp)) {
                                                Icon(Icons.Outlined.ChevronRight, contentDescription = "Next", tint = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    }
                                }
                            }
                            if (rowItems.size < 2) {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * A reusable composable for items within a settings grid.
 * It provides a consistent background, padding, and title structure.
 *
 * @param modifier Modifier for this composable.
 * @param content The main content of the setting item (e.g., a Switch, a slider, etc.).
 */
@Composable
private fun SettingGridItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        content()
    }
}

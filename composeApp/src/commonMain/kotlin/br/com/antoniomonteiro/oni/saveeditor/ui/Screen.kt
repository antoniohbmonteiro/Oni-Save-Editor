package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents all possible screens in the application.
 */
sealed interface AppScreen

/**
 * Represents the initial screen where the user can upload a file.
 */
data object UploadScreen : AppScreen

/**
 * A top-level list of all navigable screens, moved out of the companion object to avoid a potential compiler bug.
 */
val AllNavigableScreens = listOf(
    NavigableScreen.Colony,
    NavigableScreen.Duplicants,
    NavigableScreen.Resources,
    NavigableScreen.Base,
    NavigableScreen.Research,
    NavigableScreen.Geysers,
    NavigableScreen.Planetoids,
    NavigableScreen.Settings
)

/**
 * Represents a screen that is accessible via the main navigation sidebar.
 */
sealed class NavigableScreen(
    val id: String,
    val label: String,
    val icon: ImageVector
) : AppScreen {
    data object Colony : NavigableScreen("colony", "Colony Summary", Icons.Outlined.Home)
    data object Duplicants : NavigableScreen("duplicants", "Duplicants", Icons.Outlined.Groups)
    data object Resources : NavigableScreen("resources", "Resources", Icons.Outlined.Inventory2)
    data object Base : NavigableScreen("base", "Base", Icons.Outlined.Apartment)
    data object Research : NavigableScreen("research", "Research", Icons.Outlined.Science)
    data object Geysers : NavigableScreen("geysers", "Geysers", Icons.Outlined.Water)
    data object Planetoids : NavigableScreen("planetoids", "Planetoids", Icons.Outlined.Public)
    data object Settings : NavigableScreen("settings", "Settings", Icons.Outlined.Settings)
}

package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.ColonySummaryScreen

@Composable
fun MainScaffold(
    activeScreen: NavigableScreen,
    onScreenChange: (NavigableScreen) -> Unit,
    fileName: String,
    onClose: () -> Unit,
    onSaveChanges: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Sidebar(
            activeScreen = activeScreen,
            onScreenChange = onScreenChange,
            fileName = fileName,
            onClose = onClose,
            onSaveChanges = onSaveChanges
        )

        // Content Area
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopStart // Changed from Center to TopStart
        ) {
            when (activeScreen) {
                is NavigableScreen.Colony -> ColonySummaryScreen()
                is NavigableScreen.Duplicants -> Text("Duplicants Screen")
                is NavigableScreen.Resources -> Text("Resources Screen")
                is NavigableScreen.Base -> Text("Base Screen")
                is NavigableScreen.Research -> Text("Research Screen")
                is NavigableScreen.Geysers -> Text("Geysers Screen")
                is NavigableScreen.Planetoids -> Text("Planetoids Screen")
                is NavigableScreen.Settings -> Text("Settings Screen")
            }
        }
    }
}

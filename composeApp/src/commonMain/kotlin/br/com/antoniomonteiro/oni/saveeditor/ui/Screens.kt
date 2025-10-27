package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import br.com.antoniomonteiro.oni.saveeditor.ui.model.ColonyHeader

sealed interface Screen {
    data object Load : Screen
    data object Colony : Screen
    data object Duplicants : Screen
}

// ---- Load ----
@Composable
fun LoadScreen(
    error: String? = null,
    onOpen: () -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("ONI Save Editor", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Text("Open a .sav to start. You’ll land on the Colony section.")
            Spacer(Modifier.height(16.dp))
            Button(onClick = onOpen) { Text("Open .sav") }
            if (error != null) {
                Spacer(Modifier.height(12.dp))
                AssistiveNote(error)
            }
        }
    }
}

// ---- Colony ----
@Composable
fun ColonyScreen(header: ColonyHeader?) {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Colony", style = MaterialTheme.typography.titleLarge)
        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Header", style = MaterialTheme.typography.titleMedium)
                if (header == null) {
                    Text("No file loaded", fontFamily = FontFamily.Monospace)
                } else {
                    Text("Base name: ${header.baseName}")
                    Text("Original save: ${header.originalSaveName ?: "—"}")
                    Text("Cycles: ${header.numberOfCycles}")
                    Text("Duplicants: ${header.numberOfDuplicants}")
                    Text("Auto-save: ${header.isAutoSave}")
                    Text("Major.Minor: ${header.major ?: "?"}.${header.minor ?: "?"}")
                    Text("Cluster: ${header.clusterName ?: "—"}")
                    Text("Colony GUID: ${header.colonyGuid ?: "—"}")
                }
            }
        }
    }
}

// ---- Duplicants ----
@Composable
fun DuplicantsScreen() {
    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Duplicants", style = MaterialTheme.typography.titleLarge)
        AssistiveNote("Coming soon: list, filters (All / Regular / Bionic), traits.")
    }
}

@Composable
private fun AssistiveNote(text: String) {
    Surface(tonalElevation = 2.dp, shape = MaterialTheme.shapes.medium) {
        Text(
            text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

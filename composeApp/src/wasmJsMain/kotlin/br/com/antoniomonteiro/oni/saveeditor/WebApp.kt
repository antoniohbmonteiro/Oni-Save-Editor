package br.com.antoniomonteiro.oni.saveeditor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import br.com.antoniomonteiro.oni.saveeditor.save.SaveGame
import br.com.antoniomonteiro.oni.saveeditor.save.SaveHeader
import br.com.antoniomonteiro.oni.saveeditor.save.parseOniSave

@Composable
fun WebApp() {
    var save by remember { mutableStateOf<SaveGame?>(null) }
    var status by remember { mutableStateOf("Ready") }
    var error by remember { mutableStateOf<String?>(null) }
    var bytesRead by remember { mutableStateOf<Int?>(null) }
    var headerPreview by remember { mutableStateOf<String?>(null) }

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(Modifier.padding(24.dp).widthIn(max = 920.dp)) {
                Text("ONI Save Editor", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(12.dp))

                Button(onClick = {
                    status = "Waiting for file..."
                    error = null
                    save = null
                    headerPreview = null
                    bytesRead = null

                    FilePicker { bytes ->
                        if (bytes != null) {
                            bytesRead = bytes.size
                            status = "Reading header..."
                            val result = runCatching { parseOniSave(bytes) }
                            result.onSuccess { s ->
                                save = s
                                headerPreview = s.header.__rawJsonPreview
                                status = "OK"
                            }.onFailure { t ->
                                error = t.message ?: t.toString()
                                status = "Failed to parse"
                            }
                        } else {
                            error = "File selection canceled."
                            status = "Canceled/Error"
                        }
                    }.launch()
                }) { Text("Open .sav file") }

                Spacer(Modifier.height(12.dp))
                Text("Status: $status", style = MaterialTheme.typography.bodySmall)
                bytesRead?.let { Text("Bytes read: $it", style = MaterialTheme.typography.bodySmall) }
                error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                headerPreview?.let {
                    Spacer(Modifier.height(8.dp))
                    Surface(tonalElevation = 2.dp) {
                        Text(
                            "Header JSON (preview): $it…",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                save?.let { HeaderCard(it.header) }
            }
        }
    }
}

@Composable
private fun HeaderCard(h: SaveHeader) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text("Header", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Info("Base name", h.baseName)
            Info("Original save", h.originalSaveName)
            Info("Cycles", h.numberOfCycles?.toString())
            Info("Duplicants", h.numberOfDuplicants?.toString())
            Info("Auto-save", h.isAutoSave?.toString())
            Info("Save version", h.saveVersion?.toString())
            Info("Major.Minor", listOfNotNull(h.saveMajorVersion, h.saveMinorVersion).joinToString(".").ifEmpty { null })
            Info("Cluster", h.clusterName ?: h.clusterId)
            Info("Colony GUID", h.colonyGuid)
        }
    }
}

@Composable
private fun Info(label: String, value: String?) {
    if (value.isNullOrEmpty()) return
    Row(Modifier.padding(vertical = 4.dp)) {
        Text("$label: ", style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

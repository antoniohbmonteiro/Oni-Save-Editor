package br.com.antoniomonteiro.oni.saveeditor

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import br.com.antoniomonteiro.oni.saveeditor.ui.RootApp
import br.com.antoniomonteiro.oni.saveeditor.ui.model.loadColonyHeader
import java.io.File

fun main() = application {
    val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)

    Window(
        onCloseRequest = ::exitApplication,
        title = "ONI Save Editor",
        state = windowState
    ) {
        RootApp(
            onPickSav = { onLoaded, onError ->
                FilePicker { bytes ->
                    if (bytes == null) {
                        onError("Nenhum arquivo selecionado.")
                    } else {
                        runCatching { loadColonyHeader(bytes) }
                            .onSuccess(onLoaded)
                            .onFailure { error ->
                                onError(error.message ?: "Falha ao ler o arquivo.")
                            }
                    }
                }.launch(listOf("sav"))
            },
            onFileDrop = { file, onLoaded, onError ->
                val droppedFile = file as? File
                if (droppedFile == null) {
                    onError("Tipo de arquivo não suportado.")
                    return@RootApp
                }

                runCatching { loadColonyHeader(droppedFile.readBytes()) }
                    .onSuccess(onLoaded)
                    .onFailure { error ->
                        onError(error.message ?: "Falha ao ler o arquivo.")
                    }
            }
        )
    }
}

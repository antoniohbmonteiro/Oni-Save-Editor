package br.com.antoniomonteiro.oni.saveeditor

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import br.com.antoniomonteiro.oni.saveeditor.ui.MainScaffold
import br.com.antoniomonteiro.oni.saveeditor.ui.NavigableScreen
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.UploadScreen
import br.com.antoniomonteiro.oni.saveeditor.ui.theme.AppTheme
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.getElementById("compose-root")!!) {
        var currentView by remember { mutableStateOf("upload") }
        var activeScreen by remember { mutableStateOf<NavigableScreen>(NavigableScreen.Colony) }

        AppTheme {
            when (currentView) {
                "upload" -> {
                    UploadScreen(
                        onFileSelect = {
                            // TODO: Implement file picking for Wasm
                            println("File select clicked on Wasm")
                        },
                        onDemoSelect = {
                            println("Load Demo clicked on Wasm")
                            currentView = "main"
                        }
                    )
                }
                "main" -> {
                    MainScaffold(
                        activeScreen = activeScreen,
                        onScreenChange = { newScreen -> activeScreen = newScreen },
                        fileName = "Colony_Cycle_500.sav", // Placeholder
                        onClose = { currentView = "upload" },
                        onSaveChanges = { println("Save Changes clicked") }
                    )
                }
            }
        }
    }
}
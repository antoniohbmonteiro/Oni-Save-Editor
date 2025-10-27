package br.com.antoniomonteiro.oni.saveeditor

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import br.com.antoniomonteiro.oni.saveeditor.ui.MainScaffold
import br.com.antoniomonteiro.oni.saveeditor.ui.NavigableScreen
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.DragAndDropOverlay
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.UploadScreen
import br.com.antoniomonteiro.oni.saveeditor.ui.theme.AppTheme
import java.awt.datatransfer.DataFlavor
import java.io.File

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)
    var isDragging by remember { mutableStateOf(false) }
    var currentView by remember { mutableStateOf("upload") }
    var activeScreen by remember { mutableStateOf<NavigableScreen>(NavigableScreen.Colony) }

    val dragAndDropTarget = remember {
        object: DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                if (currentView == "upload") isDragging = true
            }

            override fun onEnded(event: DragAndDropEvent) {
                isDragging = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                if (currentView != "upload") return false
                event.awtTransferable.let { transferable ->
                    if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        val files = transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<File>
                        files?.firstOrNull()?.let { file ->
                            handleFile(file.readBytes()) {
                                currentView = "main"
                            }
                        }
                        isDragging = false
                        return true
                    }
                }
                isDragging = false
                return false
            }
        }
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "ONI Save Editor",
        state = windowState
    ) {
        AppTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = { event ->
                            currentView == "upload" && event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
                        },
                        target = dragAndDropTarget
                    ),
                contentAlignment = Alignment.Center
            ) {
                when (currentView) {
                    "upload" -> {
                        UploadScreen(
                            onFileSelect = {
                                FilePicker { bytes ->
                                    handleFile(bytes) {
                                        currentView = "main"
                                    }
                                }.launch(listOf("sav"))
                            },
                            onDemoSelect = {
                                println("Load Demo clicked")
                                currentView = "main"
                            }
                        )

                        if (isDragging) {
                            DragAndDropOverlay()
                        }
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
}

private fun handleFile(bytes: ByteArray?, onFileLoaded: () -> Unit) {
    if (bytes != null) {
        println("File handled! Size: ${bytes.size} bytes")
        onFileLoaded()
    } else {
        println("File handling failed or was canceled.")
    }
}

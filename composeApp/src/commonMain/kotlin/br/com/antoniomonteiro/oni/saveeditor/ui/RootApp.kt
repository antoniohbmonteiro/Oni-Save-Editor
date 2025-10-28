package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.antoniomonteiro.oni.saveeditor.ui.model.ColonyHeader
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.UploadScreen as UploadScreenComposable
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.DragAndDropOverlay
import br.com.antoniomonteiro.oni.saveeditor.ui.theme.AppTheme

@Composable
fun RootApp(
    onPickSav: (onLoaded: (ColonyHeader) -> Unit, onError: (String) -> Unit) -> Unit,
    onFileDrop: (file: Any, onLoaded: (ColonyHeader) -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _ -> }
) {
    AppTheme {
        var screen by remember { mutableStateOf<AppScreen>(UploadScreen) }
        var errorMsg by remember { mutableStateOf<String?>(null) }
        var header by remember { mutableStateOf<ColonyHeader?>(null) }
        var isDragging by remember { mutableStateOf(false) }

        when (val currentScreen = screen) {
            is NavigableScreen -> {
                MainScaffold(
                    activeScreen = currentScreen,
                    onScreenChange = { newScreen -> screen = newScreen },
                    fileName = header?.baseName ?: "Unknown File",
                    onClose = {
                        header = null
                        screen = UploadScreen
                    },
                    onSaveChanges = { println("Save Changes clicked") }
                )
            }
            is UploadScreen -> {
                FileDropTarget<Any>(
                    onDragStateChange = { isDragging = it },
                    onFileDrop = { file ->
                        onFileDrop(
                            file,
                            { h ->
                                header = h
                                errorMsg = null
                                screen = NavigableScreen.Colony
                            },
                            { msg -> errorMsg = msg }
                        )
                    }
                ) {
                    UploadScreenComposable(
                        error = errorMsg,
                        onFileSelect = {
                            onPickSav(
                                { h ->
                                    header = h
                                    errorMsg = null
                                    screen = NavigableScreen.Colony
                                },
                                { msg ->
                                    errorMsg = msg
                                    header = null
                                }
                            )
                        },
                        onDemoSelect = {
                            // TODO: Implement demo loading
                        }
                    )
                }

                if (isDragging) {
                    DragAndDropOverlay()
                }
            }

            else -> {}
        }
    }
}

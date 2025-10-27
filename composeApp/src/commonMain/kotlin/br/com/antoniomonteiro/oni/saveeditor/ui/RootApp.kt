import androidx.compose.runtime.*
import br.com.antoniomonteiro.oni.saveeditor.ui.AppScreen
import br.com.antoniomonteiro.oni.saveeditor.ui.FileDropTarget
import br.com.antoniomonteiro.oni.saveeditor.ui.MainScaffold
import br.com.antoniomonteiro.oni.saveeditor.ui.NavigableScreen
import br.com.antoniomonteiro.oni.saveeditor.ui.UploadScreen
import br.com.antoniomonteiro.oni.saveeditor.ui.model.ColonyHeader
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.UploadScreen as UploadScreenComposable
import br.com.antoniomonteiro.oni.saveeditor.ui.screen.DragAndDropOverlay
import br.com.antoniomonteiro.oni.saveeditor.ui.theme.AppTheme

@Composable
fun RootApp(
    onPickSav: (onLoaded: (ColonyHeader) -> Unit, onError: (String) -> Unit) -> Unit,
    onFileDrop: (file: Any, onLoaded: (ColonyHeader) -> Unit, onError: (String) -> Unit) -> Unit = { _, _, _ -> },
    isDragging: Boolean = false
) {
    AppTheme {
        var screen by remember { mutableStateOf<AppScreen>(UploadScreen) } // Agora usa o objeto UploadScreen
        var errorMsg by remember { mutableStateOf<String?>(null) }
        var header by remember { mutableStateOf<ColonyHeader?>(null) }

        when (val currentScreen = screen) {
            is NavigableScreen -> {
                MainScaffold(
                    activeScreen = currentScreen,
                    onScreenChange = { newScreen -> screen = newScreen },
                    fileName = header?.baseName ?: "Unknown File",
                    onClose = {
                        header = null
                        screen = UploadScreen // Navigate back to UploadScreen
                    },
                    onSaveChanges = { println("Save Changes clicked") }
                )
            }
            is UploadScreen -> { // Verifica o tipo do objeto, não da função Composable
                FileDropTarget<Any>({ file -> // Envolve a tela de upload com o alvo de drop
                    onFileDrop(file,
                        { h ->
                            header = h
                            errorMsg = null
                            screen = NavigableScreen.Colony
                        },
                        { msg -> errorMsg = msg }
                    )
                }) {
                    // A tela de upload agora é a tela inicial
                    UploadScreenComposable( // Chama a função Composable com um alias para evitar conflito
                        // Passa a mensagem de erro para a tela de upload
                        error = errorMsg,
                        onFileSelect = {
                            onPickSav(
                                { h ->
                                    header = h
                                    errorMsg = null
                                    screen = NavigableScreen.Colony // Navigate to the default main screen
                                },
                                { msg ->
                                    errorMsg = msg
                                    header = null
                                }
                            )
                        },
                        onDemoSelect = {
                            // TODO: Implementar lógica de carregar demo
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

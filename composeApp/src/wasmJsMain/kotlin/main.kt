import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import br.com.antoniomonteiro.oni.saveeditor.ui.model.ColonyHeader
import kotlinx.browser.document
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Use ComposeViewport para inicializar a aplicação. Ele substitui o CanvasBasedWindow
    // e lida com o onWasmReady internamente. O título da página agora deve ser
    // definido no arquivo index.html do seu projeto wasmJs.
    ComposeViewport {
        var isDragging by remember { mutableStateOf(false) }

        // Usamos DisposableEffect para adicionar e remover listeners de eventos do DOM.
        // É a forma idiomática de lidar com efeitos colaterais que precisam de limpeza.
        DisposableEffect(Unit) {
            // Se o body não for encontrado, saímos do efeito retornando um onDispose vazio.
            val body = document.body as? HTMLBodyElement ?: return@DisposableEffect onDispose {}

            val onDragEnter: (Event) -> Unit = { event ->
                event.preventDefault()
                isDragging = true
            }
            val onDragLeave: (Event) -> Unit = { event ->
                event.preventDefault()
                isDragging = false
            }
            val onDragOver: (Event) -> Unit = { event ->
                event.preventDefault() // Necessário para permitir o drop
            }

            body.addEventListener("dragenter", onDragEnter)
            body.addEventListener("dragleave", onDragLeave)
            body.addEventListener("dragover", onDragOver)

            onDispose {
                body.removeEventListener("dragenter", onDragEnter)
                body.removeEventListener("dragleave", onDragLeave)
                body.removeEventListener("dragover", onDragOver)
            }
        }

        RootApp(
            onPickSav = { onLoaded, onError ->
                // Lógica para o clique no botão "Select File"
                val input = document.createElement("input") as HTMLInputElement
                input.type = "file"
                input.accept = ".sav"
                input.onchange = {
                    val file = input.files?.get(0)
                    if (file != null) {
                        processFile(file, onLoaded, onError)
                    } else {
                        onError("Nenhum arquivo selecionado.")
                    }
                }
                input.click()
            },
            // Lógica para o drop do arquivo
            onFileDrop = { file, onLoaded, onError ->
                // O 'file' vem como 'Any' do RootApp, então fazemos um cast seguro para 'File'.
                val droppedFile = file as? File
                if (droppedFile == null) {
                    onError("Falha ao processar o arquivo solto.")
                    return@RootApp // Um 'return' simples sai do lambda.
                }
                isDragging = false
                processFile(droppedFile, onLoaded, onError)
            },
            isDragging = isDragging
        )
    }
}

private fun processFile(file: File, onLoaded: (ColonyHeader) -> Unit, onError: (String) -> Unit) {
    if (!file.name.endsWith(".sav")) {
        onError("Formato de arquivo inválido. Por favor, selecione um arquivo .sav.")
        return
    }

    val reader = FileReader()
    reader.onload = { event ->
        val content = reader.result
        // TODO: Implementar a lógica de parsing do arquivo .sav
        // Por enquanto, vamos simular o sucesso com dados mockados
//        console.log("Arquivo '${file.name}' lido com sucesso. Tamanho: ${(content as? org.khronos.webgl.ArrayBuffer)?.byteLength} bytes")
        val mockHeader = ColonyHeader(
            baseName = file.name.removeSuffix(".sav"),
            originalSaveName = file.name,
            numberOfCycles = 500, // Corrigido para corresponder ao modelo
            numberOfDuplicants = 12, // Corrigido para corresponder ao modelo
            isAutoSave = false,
            major = null, minor = null, clusterName = null, colonyGuid = null
        )
        onLoaded(mockHeader)
    }
    reader.onerror = {
        onError("Falha ao ler o arquivo: ${reader.error}")
    }
    reader.readAsArrayBuffer(file)
}
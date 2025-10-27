import androidx.compose.ui.window.ComposeViewport
import br.com.antoniomonteiro.oni.saveeditor.ui.RootApp
import br.com.antoniomonteiro.oni.saveeditor.ui.model.ColonyHeader
import br.com.antoniomonteiro.oni.saveeditor.ui.model.loadColonyHeader
import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.w3c.dom.HTMLInputElement
import org.w3c.files.File
import org.w3c.files.FileReader
import org.w3c.files.get

fun main() {
    ComposeViewport {
        RootApp(
            onPickSav = { onLoaded, onError ->
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
            onFileDrop = { file, onLoaded, onError ->
                val droppedFile = file as? File
                if (droppedFile == null) {
                    onError("Falha ao processar o arquivo solto.")
                    return@RootApp
                }
                processFile(droppedFile, onLoaded, onError)
            }
        )
    }
}

private fun processFile(file: File, onLoaded: (ColonyHeader) -> Unit, onError: (String) -> Unit) {
    if (!file.name.endsWith(".sav")) {
        onError("Formato de arquivo inválido. Por favor, selecione um arquivo .sav.")
        return
    }

    val reader = FileReader()
    reader.onload = {
        val arrayBuffer = reader.result as? ArrayBuffer
        if (arrayBuffer == null) {
            onError("Não foi possível ler o conteúdo do arquivo.")
            return@onload
        }

        val view = Uint8Array(arrayBuffer)
        val bytes = ByteArray(view.length) { index -> view[index] }
        runCatching { loadColonyHeader(bytes) }
            .onSuccess(onLoaded)
            .onFailure { error ->
                onError(error.message ?: "Falha ao interpretar o arquivo.")
            }
    }
    reader.onerror = {
        onError("Falha ao ler o arquivo: ${reader.error}")
    }
    reader.readAsArrayBuffer(file)
}

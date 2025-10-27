package br.com.antoniomonteiro.oni.saveeditor

import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.Event
import org.w3c.files.FileReader

@OptIn(ExperimentalWasmJsInterop::class)
 fun pickSavFile(
    onBytes: (ByteArray) -> Unit,
    onError: (String) -> Unit = {}
) {
    val input = (document.createElement("input") as HTMLInputElement).apply {
        type = "file"
        accept = ".sav"
        style.display = "none"
    }
    document.body?.appendChild(input)

    input.onchange = { _: Event ->
        val file = input.files?.item(0)
        if (file == null) {
            onError("Nenhum arquivo selecionado")
        } else {
            val fr = FileReader()
            fr.onload = {
                try {
                    val buffer = fr.result as ArrayBuffer
                    val view = Int8Array(buffer)
                    val bytes = ByteArray(view.length) { i -> view[i] }
                    onBytes(bytes)
                } catch (t: Throwable) {
                    onError("Erro ao ler conteúdo: ${t.message}")
                } finally {
                    input.parentElement?.removeChild(input)
                }
            }
            fr.onerror = {
                onError("Falha ao ler o arquivo.")
                input.parentElement?.removeChild(input)
            }
            fr.readAsArrayBuffer(file)
        }
    }

    input.click()
}

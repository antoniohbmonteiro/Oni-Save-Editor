package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.browser.document
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.DragEvent
import org.w3c.files.get

@Composable
actual fun <T> FileDropTarget(onFileDrop: (file: T) -> Unit, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()

        // Adiciona o listener de drop diretamente no body para capturar o evento.
        // O evento é um DragEvent, que já possui a propriedade dataTransfer.
        (document.body as? HTMLBodyElement)?.ondrop = { event: DragEvent ->
            event.preventDefault()
            val files = event.dataTransfer?.files
            if (files != null && files.length > 0) {
                @Suppress("UNCHECKED_CAST")
                (files[0] as? T)?.let(onFileDrop)
            }
        }
    }
}
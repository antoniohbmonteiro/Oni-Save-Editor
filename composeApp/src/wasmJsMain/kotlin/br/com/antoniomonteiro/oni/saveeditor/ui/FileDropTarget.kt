package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import kotlinx.browser.document
import org.w3c.dom.DragEvent
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.events.Event
import org.w3c.files.get

@Composable
actual fun <T> FileDropTarget(
    onDragStateChange: (Boolean) -> Unit,
    onFileDrop: (file: T) -> Unit,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
    }

    DisposableEffect(onFileDrop) {
        val body = document.body as? HTMLBodyElement ?: return@DisposableEffect onDispose {}

        val onDragEnter: (Event) -> Unit = { event ->
            event.preventDefault()
            onDragStateChange(true)
        }
        val onDragLeave: (Event) -> Unit = { event ->
            event.preventDefault()
            onDragStateChange(false)
        }
        val onDragOver: (Event) -> Unit = { event ->
            event.preventDefault()
        }
        val onDrop: (DragEvent) -> Unit = { event ->
            event.preventDefault()
            onDragStateChange(false)
            val files = event.dataTransfer?.files
            if (files != null && files.length > 0) {
                @Suppress("UNCHECKED_CAST")
                (files[0] as? T)?.let(onFileDrop)
            }
        }

        body.addEventListener("dragenter", onDragEnter)
        body.addEventListener("dragleave", onDragLeave)
        body.addEventListener("dragover", onDragOver)
        body.addEventListener("drop", onDrop)

        onDispose {
            body.removeEventListener("dragenter", onDragEnter)
            body.removeEventListener("dragleave", onDragLeave)
            body.removeEventListener("dragover", onDragOver)
            body.removeEventListener("drop", onDrop)
        }
    }
}
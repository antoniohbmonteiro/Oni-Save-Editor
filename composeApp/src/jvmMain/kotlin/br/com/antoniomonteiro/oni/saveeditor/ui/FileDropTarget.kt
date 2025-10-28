package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import java.awt.datatransfer.DataFlavor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun <T> FileDropTarget(
    onDragStateChange: (Boolean) -> Unit,
    onFileDrop: (file: T) -> Unit,
    content: @Composable () -> Unit,
) {
    val dragTarget = remember(onDragStateChange, onFileDrop) {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                onDragStateChange(true)
            }

            override fun onEnded(event: DragAndDropEvent) {
                onDragStateChange(false)
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                onDragStateChange(false)
                val transferable = event.awtTransferable
                return if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    val files = transferable.getTransferData(DataFlavor.javaFileListFlavor) as? List<*>
                    val first = files?.firstOrNull()
                    if (first != null) {
                        @Suppress("UNCHECKED_CAST")
                        onFileDrop(first as T)
                        true
                    } else {
                        false
                    }
                } else {
                    false
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .dragAndDropTarget(
                shouldStartDragAndDrop = { event ->
                    event.awtTransferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
                },
                target = dragTarget
            )
    ) {
        content()
    }
}
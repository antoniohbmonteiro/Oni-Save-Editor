package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.runtime.Composable

@Composable
actual fun <T> FileDropTarget(onFileDrop: (file: T) -> Unit, content: @Composable () -> Unit) {
    // A JVM usa seu próprio manipulador de drag-and-drop no `main.kt`, então apenas renderizamos o conteúdo.
    content()
}
package br.com.antoniomonteiro.oni.saveeditor.ui

import androidx.compose.runtime.Composable

@Composable
expect fun <T> FileDropTarget(
    onFileDrop: (file: T) -> Unit,
    content: @Composable () -> Unit,
)
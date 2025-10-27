package br.com.antoniomonteiro.oni.saveeditor

import org.jetbrains.compose.resources.stringResource
import saveeditor.composeapp.generated.resources.Res

/**
 * Handles the processing of a selected or dropped file.
 * @param bytes The content of the file as a ByteArray, or null if the file operation failed or was canceled.
 */
fun handleFile(bytes: ByteArray?) {
    if (bytes != null) {
        println("File handled! Size: ${bytes.size} bytes bytes")
        // TODO: Navigate to the next screen with the file data
    } else {
        println("File handling failed or was canceled.")
    }
}

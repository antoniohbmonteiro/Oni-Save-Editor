package br.com.antoniomonteiro.oni.saveeditor

import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * The JVM (Desktop) implementation of the FilePicker.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FilePicker actual constructor(private val onFileSelected: (ByteArray?) -> Unit) {
    actual fun launch(allowedExtensions: List<String>) {
        val fileChooser = JFileChooser()
        fileChooser.dialogTitle = "Select a .sav file"

        if (allowedExtensions.isNotEmpty()) {
            val description = allowedExtensions.joinToString(", ") { ".$it" }
            val filter = FileNameExtensionFilter(description, *allowedExtensions.toTypedArray())
            fileChooser.fileFilter = filter
        }

        val result = fileChooser.showOpenDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val file = fileChooser.selectedFile
            onFileSelected(file.readBytes())
        } else {
            onFileSelected(null)
        }
    }
}

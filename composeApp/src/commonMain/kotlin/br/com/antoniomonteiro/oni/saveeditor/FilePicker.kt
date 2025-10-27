package br.com.antoniomonteiro.oni.saveeditor

/**
 * A multiplatform abstraction for selecting a file.
 * @param onFileSelected A callback that receives the file content as a ByteArray, or null if the selection is canceled.
 */
expect class FilePicker(onFileSelected: (ByteArray?) -> Unit) {
    /**
     * Opens the operating system's file selection window/dialog.
     * @param allowedExtensions A list of allowed file extensions (e.g., listOf("sav", "txt")).
     */
    fun launch(allowedExtensions: List<String> = emptyList())
}

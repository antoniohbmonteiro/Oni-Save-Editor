package br.com.antoniomonteiro.oni.saveeditor

import kotlinx.browser.document
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileReader
import org.w3c.files.get
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

/**
 * The Web (Wasm) implementation of the FilePicker.
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class FilePicker actual constructor(private val onFileSelected: (ByteArray?) -> Unit) {
    actual fun launch(allowedExtensions: List<String>) {
        val input = document.createElement("input") as HTMLInputElement
        input.type = "file"
        input.style.display = "none"

        if (allowedExtensions.isNotEmpty()) {
            input.accept = allowedExtensions.joinToString(",") { ".$it" }
        }

        input.onchange = { _ ->
            val file = input.files?.get(0)
            if (file != null) {
                val reader = FileReader()
                reader.onload = { event ->
                    val arrayBuffer = (event.target as FileReader).result as ArrayBuffer
                    
                    val jsUint8Array = Uint8Array(arrayBuffer)
                    // Corrected: Call .get() explicitly to avoid ambiguity
                    val kotlinByteArray = ByteArray(jsUint8Array.length) { i -> jsUint8Array[i] }
                    
                    onFileSelected(kotlinByteArray)
                }
                reader.readAsArrayBuffer(file)
            } else {
                onFileSelected(null)
            }
        }

        document.body?.appendChild(input)
        input.click()
        document.body?.removeChild(input)
    }
}

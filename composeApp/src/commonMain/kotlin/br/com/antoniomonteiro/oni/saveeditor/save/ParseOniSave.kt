package br.com.antoniomonteiro.oni.saveeditor.save

import kotlinx.serialization.json.Json
import kotlin.math.min

private val json = Json { ignoreUnknownKeys = true }

/**
 * Procura o primeiro objeto JSON válido nos primeiros ~1MB do arquivo
 * (é o header do ONI) e desserializa para [SaveHeader].
 */
fun parseOniSave(bytes: ByteArray): SaveGame {
    val scanLimit = min(bytes.size, 1_000_000)
    var i = 0
    while (i < scanLimit) {
        if (bytes[i] == '{'.code.toByte()) {
            val end = findMatchingBrace(bytes, i) ?: break
            if (end > i) {
                val jsonStr = bytes.copyOfRange(i, end + 1).decodeToString()
                if (looksLikeHeader(jsonStr)) {
                    val header = json.decodeFromString(SaveHeader.serializer(), jsonStr).also {
                        it.__rawJsonPreview = jsonStr.take(120)
                    }
                    return SaveGame(header)
                }
                i = end + 1
                continue
            }
        }
        i++
    }
    error("JSON header não encontrado nos primeiros $scanLimit bytes")
}

/** Faz o balanceamento de chaves, ignorando { } dentro de strings (com escapes). */
private fun findMatchingBrace(bytes: ByteArray, start: Int): Int? {
    var depth = 0
    var inString = false
    var esc = false
    var i = start
    while (i < bytes.size) {
        val b = bytes[i].toInt() and 0xFF
        if (inString) {
            if (esc) esc = false
            else if (b == 0x5C) esc = true        // '\'
            else if (b == 0x22) inString = false  // '"'
        } else {
            when (b) {
                0x22 -> inString = true           // '"'
                0x7B -> depth++                   // '{'
                0x7D -> {                         // '}'
                    depth--
                    if (depth == 0) return i
                }
            }
        }
        i++
    }
    return null
}

/** Heurística leve para reconhecer que o objeto é o header do ONI. */
private fun looksLikeHeader(s: String): Boolean =
    s.contains("\"numberOfCycles\"") ||
            s.contains("\"saveMajorVersion\"") ||
            s.contains("\"clusterName\"") ||
            s.contains("\"saveVersion\"")

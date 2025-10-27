package br.com.antoniomonteiro.oni.saveeditor.ui.model

import br.com.antoniomonteiro.oni.saveeditor.save.parseOniSave
import br.com.antoniomonteiro.oni.saveeditor.ui.mappers.toColonyHeader

/**
 * Parses the ONI save bytes and extracts a UI-friendly [ColonyHeader].
 */
fun loadColonyHeader(bytes: ByteArray): ColonyHeader =
    parseOniSave(bytes).header.toColonyHeader()

package br.com.antoniomonteiro.oni.saveeditor.save

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class SaveHeader(
    val numberOfCycles: Int? = null,
    val numberOfDuplicants: Int? = null,
    val baseName: String? = null,
    val originalSaveName: String? = null,
    val isAutoSave: Boolean? = null,
    val saveMajorVersion: Int? = null,
    val saveMinorVersion: Int? = null,
    val clusterId: String? = null,
    val clusterName: String? = null,
    val saveVersion: Int? = null,
    val colonyGuid: String? = null,
) {
    /** Só para debug/preview na UI; não faz parte do save. */
    @Transient
    var __rawJsonPreview: String? = null
}

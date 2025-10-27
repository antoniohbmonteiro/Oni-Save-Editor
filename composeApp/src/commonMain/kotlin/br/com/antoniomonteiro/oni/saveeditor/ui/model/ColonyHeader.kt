package br.com.antoniomonteiro.oni.saveeditor.ui.model

data class ColonyHeader(
    val baseName: String,
    val originalSaveName: String?,
    val numberOfCycles: Int,
    val numberOfDuplicants: Int,
    val isAutoSave: Boolean,
    val major: Int?,
    val minor: Int?,
    val clusterName: String?,
    val colonyGuid: String?
)

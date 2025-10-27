package br.com.antoniomonteiro.oni.saveeditor.ui.mappers

import br.com.antoniomonteiro.oni.saveeditor.save.SaveHeader
import br.com.antoniomonteiro.oni.saveeditor.ui.model.ColonyHeader

/**
 * Maps the low-level [SaveHeader] from the data layer
 * to a UI-friendly [ColonyHeader] for presentation.
 */
fun SaveHeader.toColonyHeader(): ColonyHeader = ColonyHeader(
    baseName = baseName ?: "—",
    originalSaveName = originalSaveName,
    numberOfCycles = numberOfCycles ?: 0,
    numberOfDuplicants = numberOfDuplicants ?: 0,
    isAutoSave = isAutoSave ?: false,
    major = saveMajorVersion,
    minor = saveMinorVersion,
    clusterName = clusterName ?: clusterId,
    colonyGuid = colonyGuid
)

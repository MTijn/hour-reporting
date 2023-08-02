package nl.martijnklene.hourreporting.controllers.dto

import java.util.*

data class IgnoredCategory(
    val id: UUID,
    val displayName: String?,
    val ignored: Boolean
)

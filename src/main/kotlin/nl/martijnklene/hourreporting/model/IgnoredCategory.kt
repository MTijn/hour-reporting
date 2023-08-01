package nl.martijnklene.hourreporting.model

import java.util.*

data class IgnoredCategory(
    val id: UUID,
    val userId: UUID,
    val outlookCategoryId: UUID,
    val name: String
)

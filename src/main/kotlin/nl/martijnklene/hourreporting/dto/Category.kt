package nl.martijnklene.hourreporting.dto

import java.util.UUID

data class Category(
    val id: UUID,
    val displayName: String?,
    val isDefault: Boolean = false,
    val jiraTicketId: String?
)

package nl.martijnklene.hourreporting.controllers.dto

import java.util.UUID

data class Category(
    val id: UUID,
    val displayName: String?,
    val isDefault: Boolean = false,
    val jiraTicketId: Int?,
    val jiraKey: String?
)

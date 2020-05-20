package nl.martijnklene.hourreporting.application.model

import java.util.*

data class Category(val id: UUID, val userId: UUID, val name: String, val clockifyProjectId: String, val default: Boolean)

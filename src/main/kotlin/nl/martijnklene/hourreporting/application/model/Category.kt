package nl.martijnklene.hourreporting.application.model

import java.util.*

data class Category(val userId: UUID, val outlookTaskId: UUID?, val clockifyProjectId: String, val default: Boolean)

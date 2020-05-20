package nl.martijnklene.hourreporting.application.model

import java.util.*

data class User(val id: UUID, val name: String, val mappedCategories: List<Category>)

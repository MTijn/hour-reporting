package nl.martijnklene.hourreporting.application.repository

import nl.martijnklene.hourreporting.application.model.Category
import java.util.*

interface CategoryRepository {
    fun save(category: Category)
    fun deleteCategoryById(id: UUID)
    fun deleteCategoriesFromUserId(id: UUID)
}

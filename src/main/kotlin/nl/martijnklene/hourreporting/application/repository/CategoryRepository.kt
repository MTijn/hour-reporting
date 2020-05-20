package nl.martijnklene.hourreporting.application.repository

import nl.martijnklene.hourreporting.application.model.Category
import java.util.*

interface CategoryRepository {
    fun save(category: Category)
    fun findCategoriesByUserId(userId: UUID): List<Category>
    fun deleteCategoryById(id: UUID)
    fun deleteCategoriesFromUserId(userId: UUID)
}

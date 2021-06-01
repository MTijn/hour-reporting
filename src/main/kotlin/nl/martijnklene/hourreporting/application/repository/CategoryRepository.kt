package nl.martijnklene.hourreporting.application.repository

import nl.martijnklene.hourreporting.application.model.Category
import java.util.*

interface CategoryRepository {
    fun save(category: Category)
    fun findCategoriesByUserId(userId: UUID): List<Category>
    fun findCategoryByUserIdAndTask(userId: UUID, task: String): Category?
    fun findDefaultCategoryForUser(userId: UUID): Category?
    fun deleteCategoriesFromUserId(userId: UUID)
}

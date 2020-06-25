package nl.martijnklene.hourreporting.application.repository

import nl.martijnklene.hourreporting.application.model.IgnoredCategory
import java.util.*

interface IgnoredCategoryRepository {
    fun save(category: IgnoredCategory)
    fun findIgnoredCategoriesByUserId(userId: UUID): List<IgnoredCategory>
    fun deleteIgnoredCategoryById(id: UUID)
    fun deleteIgnoredCategoriesFromUserId(userId: UUID)
}

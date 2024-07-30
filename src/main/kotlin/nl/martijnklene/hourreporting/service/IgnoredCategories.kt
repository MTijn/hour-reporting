package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.repository.CategoryRepository
import org.springframework.stereotype.Component

@Component
class IgnoredCategories(
    private val categoryRepository: CategoryRepository
) {
    fun shouldCategoryBeIgnored(
        category: String?,
        user: User
    ): Boolean {
        val ignoredCategories = categoryRepository.findIgnoredCategoriesForUser(user)
        return ignoredCategories.any { it.name == category }
    }
}

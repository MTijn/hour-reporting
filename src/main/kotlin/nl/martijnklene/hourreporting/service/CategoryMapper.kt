package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.model.User
import nl.martijnklene.hourreporting.repository.CategoryRepository
import org.springframework.stereotype.Component

@Component
class CategoryMapper(
    private val categoryRepository: CategoryRepository
) {
    fun mapCategoryToJiraTaskId(task: String?, user: User): String {
        val categories = categoryRepository.findConfiguredCategoriesForUser(user)
        return categories.firstOrNull { it.categoryName == task }?.jiraProjectId
            ?: categories.firstOrNull { it.default }?.jiraProjectId.orEmpty()
    }
}

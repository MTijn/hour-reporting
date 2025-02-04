package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.model.User
import org.springframework.stereotype.Component

@Component
class CategoryMapper() {
    fun mapCategoryToJiraTaskId(
        task: String?,
        user: User
    ): Int? {
        val categories = user.categories
        return categories.firstOrNull { it.categoryName == task }?.id
            ?: categories.firstOrNull { it.default }?.id
    }

    fun mapCategoryToJiraKey(
        task: String?,
        user: User
    ): String? {
        val categories = user.categories
        return categories.firstOrNull { it.categoryName == task }?.jiraKey
            ?: categories.firstOrNull { it.default }?.jiraKey
    }
}

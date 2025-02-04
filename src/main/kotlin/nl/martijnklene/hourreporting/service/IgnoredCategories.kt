package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.model.User
import org.springframework.stereotype.Component

@Component
class IgnoredCategories() {
    fun shouldCategoryBeIgnored(
        category: String?,
        user: User
    ): Boolean {
        return user.ignoredCategories.any { it.name == category }
    }
}

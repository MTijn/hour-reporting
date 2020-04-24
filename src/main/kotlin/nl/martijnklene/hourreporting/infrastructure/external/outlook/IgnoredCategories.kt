package nl.martijnklene.hourreporting.infrastructure.external.outlook

import org.springframework.stereotype.Component

@Component
class IgnoredCategories {
    companion object {
        val categoriesToIgnore = listOf<String>(
            "Lunch",
            "Green Category"
        )
    }

    fun shouldCategoryBeIgnored(category: String?): Boolean {
        if (category.isNullOrEmpty()) {
            return false
        }
        return (categoriesToIgnore.contains(category))
    }
}
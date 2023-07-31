package nl.martijnklene.hourreporting.service

import org.springframework.stereotype.Component

@Component
class CategoryMapper {
    companion object {
        val mappedCategories: Map<String, String> = mapOf(
            Pair("Training", "TTME-65"),
            Pair("Holiday", "TTME-63")
        ).withDefault {
            "TTME-64"
        }
    }

    fun mapCategoryToJiraTaskId(task: String?): String {
        if (task.isNullOrEmpty()) {
            return mappedCategories.getValue("empty")
        }
        return mappedCategories.getValue(task)
    }
}

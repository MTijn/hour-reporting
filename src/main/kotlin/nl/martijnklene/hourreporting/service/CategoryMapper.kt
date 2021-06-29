package nl.martijnklene.hourreporting.service

import org.springframework.stereotype.Component

@Component
class CategoryMapper {
    companion object {
        val mappedCategories: Map<String, String> = mapOf(
            Pair("Training", "5bdb1a77b07987515bef4cda"),
            Pair("Holiday", "5bd8c94db07987515bea382e")
        ).withDefault {
            "5bd8c8deb07987515bea3750"
        }
    }

    fun mapCategoryToClockifyTaskId(task: String?): String {
        if (task.isNullOrEmpty()) {
            return mappedCategories.getValue("empty")
        }
        return mappedCategories.getValue(task)
    }
}

package nl.martijnklene.hourreporting.service

import nl.martijnklene.hourreporting.application.model.Category
import nl.martijnklene.hourreporting.application.repository.CategoryRepository
import nl.martijnklene.hourreporting.infrastructure.external.outlook.UserProvider
import nl.martijnklene.hourreporting.infrastructure.http.dto.CategoriesDto
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoryMapperService(
    val categoryRepository: CategoryRepository,
    val userSession: UserSession,
    val userProvider: UserProvider
) {
    fun mapTaskToCategory(user: UUID, task: String): Category {
        return categoryRepository.findCategoryByUserIdAndTask(user, task)
            ?: categoryRepository.findDefaultCategoryForUser(user)
            ?: throw RuntimeException("There should always be a category at this point")
    }

    fun translateDtoToCategory(dto: CategoriesDto): List<Category> {
        val categories = mutableListOf<Category>()
        val user = userProvider.findOutlookUser()
        dto.categories!!.forEach {
            when (it.key) {
                "default" -> categories.add(Category(
                    UUID.randomUUID(),
                    UUID.fromString(user.id),
                    null,
                    it.value!!,
                    true
                ))
                else -> {
                    if (it.value != "Do not map") {
                        categories.add(Category(
                            UUID.randomUUID(),
                            UUID.fromString(user.id),
                            UUID.fromString(it.key!!),
                            it.value!!,
                            false
                        ))
                    }
                }
            }
        }
        return categories
    }
}

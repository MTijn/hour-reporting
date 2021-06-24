package nl.martijnklene.hourreporting.infrastructure.external.outlook

import nl.martijnklene.hourreporting.infrastructure.external.outlook.dto.Category
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoriesProvider(private val graphServiceClientProvider: GraphClientProvider) {
    fun findCategoriesForAuthenticatedUser(authentication: Authentication): List<Category> {
        return graphServiceClientProvider.provideGraphClient()
            .me()
            .outlook()
            .masterCategories()
            .buildRequest()
            .get()!!.
            currentPage.map { Category(UUID.fromString(it.id), it.displayName!!, it.color!!.name)  }
    }
}

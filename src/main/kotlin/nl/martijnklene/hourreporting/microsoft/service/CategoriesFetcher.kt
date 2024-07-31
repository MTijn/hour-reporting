package nl.martijnklene.hourreporting.microsoft.service

import com.microsoft.graph.core.tasks.PageIterator
import com.microsoft.graph.models.OutlookCategory
import com.microsoft.graph.models.OutlookCategoryCollectionResponse
import nl.martijnklene.hourreporting.microsoft.builder.GraphClientBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component
import java.util.*

@Component
class CategoriesFetcher(
    private val graphClientBuilder: GraphClientBuilder
) {
    fun findCategoriesDefinedByTheUser(client: OAuth2AuthorizedClient): Collection<OutlookCategory> {
        val categories = mutableListOf<OutlookCategory>()

        val graphClient = graphClientBuilder.buildGraphClient(client)
        val response = graphClient.me().outlook().masterCategories().get()

        PageIterator.Builder<OutlookCategory, OutlookCategoryCollectionResponse>()
            .client(graphClient)
            .collectionPage(Objects.requireNonNull(response))
            .collectionPageFactory(OutlookCategoryCollectionResponse::createFromDiscriminatorValue)
            .processPageItemCallback { category ->
                categories.add(category)
            }
            .build()
            .iterate()
        return categories
    }
}

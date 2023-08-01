package nl.martijnklene.hourreporting.microsoft.service

import com.microsoft.graph.models.OutlookCategory
import nl.martijnklene.hourreporting.microsoft.builder.GraphClientBuilder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.stereotype.Component

@Component
class CategoriesFetcher(
    private val graphClientBuilder: GraphClientBuilder
) {
    fun findCategoriesDefinedByTheUser(client: OAuth2AuthorizedClient): Collection<OutlookCategory> {
        return graphClientBuilder.buildGraphClient(client).me().outlook().masterCategories().buildRequest()
            .get()!!.currentPage
    }
}

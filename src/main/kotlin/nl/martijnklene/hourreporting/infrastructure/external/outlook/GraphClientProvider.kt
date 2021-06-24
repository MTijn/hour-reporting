package nl.martijnklene.hourreporting.infrastructure.external.outlook

import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request
import org.springframework.stereotype.Component

@Component
class GraphClientProvider() {
    fun provideGraphClient(): GraphServiceClient<Request> {
        val provider =
        return GraphServiceClient
            .builder()
            .authenticationProvider()
            .buildClient()
    }
}

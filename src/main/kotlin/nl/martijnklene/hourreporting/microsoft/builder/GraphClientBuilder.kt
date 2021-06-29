package nl.martijnklene.hourreporting.microsoft.builder

import com.microsoft.graph.requests.GraphServiceClient
import okhttp3.Request
import org.springframework.stereotype.Component

@Component
class GraphClientBuilder {
    fun buildGraphClient(): GraphServiceClient<Request> {
        return GraphServiceClient.builder().buildClient()
    }
}

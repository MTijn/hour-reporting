package nl.martijnklene.hourreporting.infrastructure.configuration

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;

class SimpleAuthProvider(private val accessToken: String): IAuthenticationProvider {
    override fun authenticateRequest(request: IHttpRequest) {
        request.addHeader("Authorization", "Bearer $accessToken")
    }
}

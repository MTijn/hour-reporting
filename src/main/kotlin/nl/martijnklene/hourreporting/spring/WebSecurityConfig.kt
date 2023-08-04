package nl.martijnklene.hourreporting.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class WebSecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { request ->
            request.requestMatchers("/actuator").permitAll()
            request.requestMatchers("/actuator/**").permitAll()
        }
        return http.build()
    }
}

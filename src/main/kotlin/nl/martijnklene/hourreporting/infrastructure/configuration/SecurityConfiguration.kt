package nl.martijnklene.hourreporting.infrastructure.configuration

import com.azure.spring.aad.webapp.AADWebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class AADOAuth2LoginSecurityConfig : AADWebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.authorizeRequests()
            .anyRequest().authenticated()
    }
}

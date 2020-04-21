package nl.martijnklene.hourreporting.infrastructure.configuration

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import java.net.URLEncoder


@Configuration
@EnableOAuth2Sso
@Order(value = 0)
class AppConfiguration : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    public override fun configure(http: HttpSecurity) {
        http.antMatcher("/**")
            .authorizeRequests()
            .antMatchers("/login**", "/error**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .logout()
            .deleteCookies()
            .invalidateHttpSession(true)
    }
}

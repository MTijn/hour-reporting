package nl.martijnklene.hourreporting.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver


@Configuration
class FreemarkerConfiguration {
    @Bean
    fun freemarkerViewResolver(): FreeMarkerViewResolver {
        val resolver = FreeMarkerViewResolver()
        resolver.isCache = true
        resolver.setPrefix("")
        resolver.setSuffix(".ftl")
        return resolver
    }

    @Bean
    fun freemarkerConfig(): FreeMarkerConfigurer {
        val freeMarkerConfigurer = FreeMarkerConfigurer()
        freeMarkerConfigurer.setTemplateLoaderPath("/templates")
        return freeMarkerConfigurer
    }
}

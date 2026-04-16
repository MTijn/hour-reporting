package nl.martijnklene.hourreporting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@EnableMethodSecurity
@SpringBootApplication
class HourReportingApplication

fun main(args: Array<String>) {
    runApplication<HourReportingApplication>(*args)
}

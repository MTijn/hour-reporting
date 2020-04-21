package nl.martijnklene.hourreporting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HourReportingApplication

fun main(args: Array<String>) {
	runApplication<HourReportingApplication>(*args)
}

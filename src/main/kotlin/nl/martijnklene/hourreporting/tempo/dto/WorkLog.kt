package nl.martijnklene.hourreporting.tempo.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime

data class WorkLog(
    val timeSpentSeconds: Int,
    val comment: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Amsterdam")
    val started: ZonedDateTime,
)

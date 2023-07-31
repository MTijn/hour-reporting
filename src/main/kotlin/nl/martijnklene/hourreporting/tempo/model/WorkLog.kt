package nl.martijnklene.hourreporting.tempo.model

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime

data class WorkLog(
    val billableSeconds: Int,
    val timeSpent: String,
    val issue: Issue,
    val timeSpentInSeconds: Int,
    val tempoWorkLogId: Int,
    val comment: String,
    val location: Location,
    val attributes: Map<String, Any>,
    val originTaskId: Int,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Europe/Amsterdam") val dateCreated:
        ZonedDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Europe/Amsterdam") val dateUpdated:
        ZonedDateTime,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "Europe/Amsterdam") val started:
        ZonedDateTime,
    val worker: String,
    val updater: String,
    val originId: Int
)

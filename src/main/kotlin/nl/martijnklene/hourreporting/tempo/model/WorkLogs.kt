package nl.martijnklene.hourreporting.tempo.model

data class WorkLogs(
    val self: String,
    val metadata: LinkedHashMap<String, Int>,
    val results: List<WorkLog>,
)

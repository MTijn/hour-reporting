package nl.martijnklene.hourreporting.infrastructure.http.dto

data class FormEntity(
    var hours: Map<String, Map<String, String>>?
)

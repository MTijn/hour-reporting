package nl.martijnklene.hourreporting.controllers.response

data class PostedHours(
    var hours: Map<String, Map<String, String>>?
)

package nl.martijnklene.hourreporting.controllers.response

data class PostedHours(
    var hours: HashMap<String, HashMap<String, String>>?
)

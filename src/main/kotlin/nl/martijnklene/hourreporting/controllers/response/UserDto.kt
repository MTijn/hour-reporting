package nl.martijnklene.hourreporting.controllers.response

data class UserDto(
    val jiraApiKey: String,
    val jiraUserName: String,
    val tempoApiKey: String,
)

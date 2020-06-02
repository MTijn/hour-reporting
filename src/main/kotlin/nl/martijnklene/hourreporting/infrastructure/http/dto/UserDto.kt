package nl.martijnklene.hourreporting.infrastructure.http.dto

data class UserDto(
    val categories: List<String>,
    val apiKey: String?
)

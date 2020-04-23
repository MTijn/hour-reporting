package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

data class Membership(
    val userId: String,
    val hourlyRate: String?,
    val targetId: String,
    val membershipType: String,
    val membershipStatus: String
)

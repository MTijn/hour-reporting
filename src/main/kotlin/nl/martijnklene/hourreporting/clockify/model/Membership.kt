package nl.martijnklene.hourreporting.clockify.model

data class Membership(
    val userId: String,
    val hourlyRate: String?,
    val targetId: String,
    val membershipType: String,
    val membershipStatus: String
)

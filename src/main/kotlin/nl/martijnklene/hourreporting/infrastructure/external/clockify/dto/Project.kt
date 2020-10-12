package nl.martijnklene.hourreporting.infrastructure.external.clockify.dto

import java.time.Duration

data class Project(
    val id: String,
    val name: String,
    val hourlyRate: String?,
    val clientId: String,
    val workspaceId: String,
    val billable: Boolean,
    val memberships: List<Membership>,
    val color: String,
    val estimate: Estimate?,
    val archived: Boolean,
    val tasks: List<Task>,
    val duration: Duration?,
    val clientName: String?,
    val note: String?,
    val template: Boolean,
    val public: Boolean
)

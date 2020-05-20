package nl.martijnklene.hourreporting.application.repository

import nl.martijnklene.hourreporting.application.model.User
import java.util.*

interface UserRepository {
    fun save(user: User)
    fun findOneUserById(id: UUID): User?
    fun deleteUserById(id: UUID)
}

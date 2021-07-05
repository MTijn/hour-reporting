package nl.martijnklene.hourreporting.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.security.Principal
import java.util.*

@Component
class UserSession {
    fun provideUserId(): UUID {
        val principal = getAuthentication()
        return UUID.randomUUID()
     }

    private fun getAuthentication(): Principal {
        val context = SecurityContextHolder.getContext()
        return context.authentication.principal as Principal
    }
}

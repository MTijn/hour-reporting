package nl.martijnklene.hourreporting.infrastructure.repository

import nl.martijnklene.hourreporting.application.model.IgnoredCategory
import nl.martijnklene.hourreporting.application.repository.IgnoredCategoryRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class IgnoredCategoryPostgresRepository(private val jdbcTemplate: NamedParameterJdbcTemplate): IgnoredCategoryRepository {
    override fun save(category: IgnoredCategory) {
    }

    override fun findIgnoredCategoriesByUserId(userId: UUID): List<IgnoredCategory> {
        return emptyList()
    }

    override fun deleteIgnoredCategoryById(id: UUID) {
    }

    override fun deleteIgnoredCategoriesFromUserId(userId: UUID) {
    }
}

package nl.martijnklene.hourreporting.infrastructure.repository

import nl.martijnklene.hourreporting.application.model.User
import nl.martijnklene.hourreporting.application.repository.CategoryRepository
import nl.martijnklene.hourreporting.application.repository.UserRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class UserPostgresRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val categoryRepository: CategoryRepository
): UserRepository {
    @Throws(Exception::class)
    override fun save(user: User) {
        val parameterSource = MapSqlParameterSource()
            .addValue("id", user.id)
            .addValue("name", user.name)
            .addValue("api_key", user.clockifyApiKey)
        jdbcTemplate.update(
            "insert into \"user\" (\"id\", \"name\", \"clockify_api_key\") values (:id, :name, :api_key)",
            parameterSource
        )
    }

    @Throws(Exception::class)
    override fun findOneUserById(id: UUID): User? {
        val parameterSource = MapSqlParameterSource().addValue("id", id)
        return jdbcTemplate.query("select id, name, clockify_api_key from \"user\" where id = :id", parameterSource) {
            resultSet, _ ->
            return@query User(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                categoryRepository.findCategoriesByUserId(
                    UUID.fromString(resultSet.getString("id"))
                ),
                resultSet.getString("clockify_api_key")
            )
        }.firstOrNull()
    }

    @Throws(Exception::class)
    override fun deleteUserById(id: UUID) {
        val parameterSource = MapSqlParameterSource()
            .addValue("id", id.toString())
        jdbcTemplate.update("delete from \"user\" where id = :id", parameterSource)
    }
}

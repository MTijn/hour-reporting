package nl.martijnklene.hourreporting.infrastructure.repository

import nl.martijnklene.hourreporting.application.model.User
import nl.martijnklene.hourreporting.application.repository.UserRepository
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.*


class UserPostgresRepository(private val jdbcTemplate: NamedParameterJdbcTemplate): UserRepository {
    @Throws(Exception::class)
    override fun save(user: User) {
        val parameterSource = MapSqlParameterSource()
            .addValue("id", user.id.toString())
            .addValue("name", user.name)
        jdbcTemplate.update("insert into user (id, name) values (:id, :name)", parameterSource)
    }

    @Throws(Exception::class)
    override fun findOneUserById(id: UUID): User? {
        val parameterSource = MapSqlParameterSource()
        parameterSource.addValue("id", id.toString())
        return jdbcTemplate.query("select id, name, clockify_api_key from user where id = :id", parameterSource) {
            resultSet, _ ->
            return@query User(
                UUID.fromString(resultSet.getString("id")),
                resultSet.getString("name"),
                emptyList(),
                resultSet.getString("clockify_api_key")
            )
        }.firstOrNull()
    }

    @Throws(Exception::class)
    override fun deleteUserById(id: UUID) {
        val parameterSource = MapSqlParameterSource()
            .addValue("id", id.toString())
        jdbcTemplate.update("delete from user where id = :id", parameterSource)
    }
}

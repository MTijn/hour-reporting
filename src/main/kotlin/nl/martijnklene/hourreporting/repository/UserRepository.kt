package nl.martijnklene.hourreporting.repository

import com.fasterxml.jackson.databind.ObjectMapper
import nl.martijnklene.hourreporting.model.User
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.util.*

@Repository
class UserRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    private val objectMapper: ObjectMapper
) {
    private val rowMapper =
        RowMapper { resultSet: ResultSet, _: Int ->
            objectMapper.readValue(resultSet.getString("data"), User::class.java)
        }

    @Throws(Exception::class)
    fun save(user: User) {
        val parameterSource =
            MapSqlParameterSource()
                .addValue("id", user.id.toString())
                .addValue("data", objectMapper.writeValueAsString(user))

        jdbcTemplate.update(
            "insert into `user` (`id`, `data`) values (:id, :data)",
            parameterSource
        )
    }

    @Throws(Exception::class)
    fun findUserById(id: UUID): User? {
        val parameterSource = MapSqlParameterSource().addValue("id", id.toString())
        return jdbcTemplate.query(
            "select * from user where id = :id",
            parameterSource,
            rowMapper
        ).firstOrNull()
    }

    @Throws(Exception::class)
    fun deleteUserById(id: UUID) {
        val parameterSource =
            MapSqlParameterSource()
                .addValue("id", id.toString())
        jdbcTemplate.update("delete from `user` where id = :id", parameterSource)
    }
}

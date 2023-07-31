package nl.martijnklene.hourreporting.spring

import org.flywaydb.core.Flyway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class DatabaseMigrationConfiguration {
    @Bean(initMethod = "migrate")
    fun flyway(dataSource: DataSource): Flyway {
        return Flyway.configure()
            .baselineOnMigrate(true)
            .dataSource(dataSource)
            .locations("classpath:db/migration/mysql")
            .load()
    }
}

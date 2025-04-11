package eni.it.gsrestservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:01 </br>
 */
@Configuration
@RequiredArgsConstructor
public class PostgresConfig {

    private final Environment environment;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.postgres.driver-class-name")));
        dataSource.setUrl(environment.getProperty("spring.datasource.postgres.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.postgres.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource.postgres.password"));
        dataSource.setConnectionProperties(additionalProperties());
        return dataSource;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", environment.getProperty("spring.jpa.show-sql", "false"));
        properties.setProperty("hibernate.format_sql", environment.getProperty("spring.jpa.properties.hibernate.format_sql", "false"));
        return properties;
    }
}

package eni.it.gsrestservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 13:58 </br>
 */
@Configuration
@RequiredArgsConstructor
public class OracleConfig {

    private final Environment environment;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.oracle.driver-class-name")));
        dataSource.setUrl(environment.getProperty("spring.datasource.oracle.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.oracle.username"));
        dataSource.setPassword(new String(java.util.Base64.getUrlDecoder()
                .decode(environment.getProperty("spring.datasource.oracle.password"))));
        return dataSource;
    }
}

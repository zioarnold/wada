package eni.it.gsrestservice.config;


import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zio Arnold aka Arni
 * @created 14/04/2025 - 00:23 </br>
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.oracle")
public class OracleDataSourceProperties extends DataSourceProperties {
}

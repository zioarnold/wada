package eni.it.gsrestservice.config;


import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author Zio Arnold aka Arni
 * @created 14/04/2025 - 00:05 </br>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "eni.it.gsrestservice.repos.ora",
        entityManagerFactoryRef = "oracleEntityManagerFactory",
        transactionManagerRef = "oracleTransactionManager"
)
public class OracleDbConfig {
    @Bean
    public DataSource oracleDataSource(Environment environment) {
        String encodedPassword = environment.getProperty("spring.datasource.oracle.password");
        String decodedPassword = new String(Base64.getDecoder().decode(encodedPassword));

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(Objects.requireNonNull(environment.getProperty("spring.datasource.oracle.driver-class-name")));
        dataSource.setUrl(environment.getProperty("spring.datasource.oracle.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource.oracle.username"));
        dataSource.setPassword(decodedPassword);
        return dataSource;
    }

    @Bean(name = "oracleJpaVendorAdapter")
    public JpaVendorAdapter oracleJpaVendorAdapter() {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(false);
        adapter.setDatabasePlatform("org.hibernate.dialect.Oracle12cDialect");
        return adapter;
    }

    @Bean(name = "oracleEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
            @Qualifier("oracleJpaVendorAdapter") JpaVendorAdapter jpaVendorAdapter,
            ObjectProvider<PersistenceUnitManager> persistenceUnitManager) {
        return new EntityManagerFactoryBuilder(
                jpaVendorAdapter,
                new HashMap<>(),
                persistenceUnitManager.getIfAvailable()
        );
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory(
            @Qualifier("oracleDataSource") DataSource dataSource,
            @Qualifier("oracleEntityManagerFactoryBuilder") EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .packages("it.eni.gsrestservice.entities.oracle")
                .persistenceUnit("oraclePU")
                .build();
    }

    @Bean
    public PlatformTransactionManager oracleTransactionManager(
            @Qualifier("oracleEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

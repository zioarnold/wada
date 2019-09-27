package eni.it.gsrestservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
@ComponentScan(basePackages = "eni.it.gsrestservice.**")
@PropertySource("classpath:application.properties")
@EnableLdapRepositories(basePackages = "eni.it.gsrestservice.**")
public class Config {
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}

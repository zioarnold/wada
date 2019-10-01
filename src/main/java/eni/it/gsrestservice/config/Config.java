package eni.it.gsrestservice.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;


@ComponentScan(basePackages = "eni.it.gsrestservice.**")
@EnableJpaRepositories("eni.it.gsrestservice.***")
@EntityScan("eni.it.gsrestservice.**")
public class Config {
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}

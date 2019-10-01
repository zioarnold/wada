package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.LDAPConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Configuration
@RestController
@PropertySource("classpath:config.properties")
public class LDAPController {
    @Autowired
    private Environment environment;

    @GetMapping("/searchuseronldappage")
    public ModelAndView searchUserOnLDAPPage() {
        return new ModelAndView("searchUserOnLdap");
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @RequestMapping(value = "/searchuseronldap")
    public ModelAndView searchUserOnLdap(HttpServletRequest request, @RequestParam(required = false, name = "userID") String userID) {
        LDAPConnector ldapConnector = new LDAPConnector();
        request.setAttribute("userIDDATA", ldapConnector.searchOnLDAP(userID,
                environment.getProperty("vds.ldapURL"),
                environment.getProperty("vds.userName"),
                environment.getProperty("vds.password"),
                environment.getProperty("vds.baseDN")));
        return new ModelAndView("searchUserOnLdap");
    }
}

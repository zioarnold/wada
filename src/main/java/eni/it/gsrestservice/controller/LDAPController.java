package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.LDAPConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class LDAPController {
    @Autowired
    private Environment environment;

    @GetMapping("/searchuseronldappage")
    public ModelAndView searchUserOnLDAPPage(HttpServletRequest request) {
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        return new ModelAndView("searchUserOnLdap");
    }

    @RequestMapping(value = "/searchuseronldap")
    public ModelAndView searchUserOnLdap(HttpServletRequest request, @RequestParam(required = false, name = "userID") String userID) throws IOException {
        LDAPConnector ldapConnector = new LDAPConnector();
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        request.setAttribute("userIDDATA", ldapConnector.searchOnLDAP(userID));
        return new ModelAndView("searchUserOnLdap");
    }
}

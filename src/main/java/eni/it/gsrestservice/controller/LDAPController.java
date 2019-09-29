package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.LDAPConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LDAPController {
    private LDAPConnector ldapConnector;

    @GetMapping("/searchuseronldappage")
    public ModelAndView searchUserOnLDAPPage() {
        return new ModelAndView("searchUserOnLdap");
    }

    @RequestMapping(value = "/searchuseronldap")
    public ModelAndView searchUserOnLdap(HttpServletRequest request, @RequestParam(required = false, name = "userID") String userID) {
        ldapConnector = new LDAPConnector();
        ldapConnector.connectToLDAP("ldap://vds-test.services.eni.intranet:18389/", "cn=adminLDAP-QLIK,ou=guest,o=ea-tree"
                , "B3%jUNwb", "dc=pri");
        request.setAttribute("userIDDATA", ldapConnector.searchOnLDAP(userID));
        return new ModelAndView("searchUserOnLdap");
    }
}

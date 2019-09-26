package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.service.LDAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LDAPController {
    @Autowired
    LDAPService ldapService;

    @GetMapping("/searchuseronldap")
    public ModelAndView searchUserOnLdap(HttpServletRequest request, @RequestParam(name = "ldap_user_filter") String filter) {
        request.setAttribute("ldapuser", ldapService.searchUserOnLdap(filter));
        return new ModelAndView("searchUserOnLdap");
    }

}

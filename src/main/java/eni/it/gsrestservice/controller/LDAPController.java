package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.service.LDAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LDAPController {
    @Autowired
    LDAPService ldapService;

    @GetMapping("/searchallusersfromldap")
    public ModelAndView findAllUsersFromLDAP(HttpServletRequest request) {
        request.setAttribute("qusers", ldapService.getAllUsers());
        return new ModelAndView("allQUsersFromDB");
    }

}

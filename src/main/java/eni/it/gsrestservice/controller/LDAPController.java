package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.dao.LDAPRepository;
import eni.it.gsrestservice.service.LDAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class LDAPController {
    @Autowired
    LDAPRepository ldapRepository;

    LDAPService ldapService;

    @GetMapping("/searchuseronldappage")
    public ModelAndView searchUserOnLDAPPage() {
        return new ModelAndView("searchUserOnLdap");
    }

//    @RequestMapping(value = "/searchuseronldap", method = RequestMethod.GET)
//    public ModelAndView searchUserOnLdap(HttpServletRequest request, @RequestParam(name = "ldap_user_filter") String filter) {
//        request.setAttribute("ldapuser", ldapService.findByEniDesignatedNumber(filter));
//        return new ModelAndView("searchUserOnLdap");
//    }

    @RequestMapping(value = "/searchuseronldap", method = RequestMethod.GET)
    public List<String> searchUserOnLdap(@RequestParam(name = "userID") String userID) {
        return ldapService.findByEniDesignatedNumber(userID);
    }

    @GetMapping("/allusersfromldap")
    public ModelAndView getAllUsersFromLDAP(HttpServletRequest request) {
        request.setAttribute("ldapusers", ldapService.findAll());
        return new ModelAndView("allUsersFromLDAP");
    }
}

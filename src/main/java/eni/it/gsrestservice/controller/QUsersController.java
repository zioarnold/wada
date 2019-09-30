package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.service.QUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class QUsersController {
    @Autowired
    QUsersService qUsersService;

    @GetMapping("/allqlikusersfromdb")
    public ModelAndView allQUsersFromDB(HttpServletRequest request) {
        request.setAttribute("qusers", qUsersService.getAllUsers());
        return new ModelAndView("allQUsersFromDB");
    }

    @RequestMapping("/searchquserondb")
    public ModelAndView searchQUserOnDB(HttpServletRequest request, @RequestParam(required = false, name = "quser_filter") String userId) {
        request.setAttribute("quser_filter", qUsersService.findQUser(userId.toUpperCase()));
        return new ModelAndView("searchQUserOnDB");
    }

    @GetMapping("/searchquserondbpage")
    public ModelAndView searchQUserOnDBPage() {
        return new ModelAndView("searchQUserOnDB");
    }
}

package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {
    @Autowired
    private UsersService usersService;

    @GetMapping("/allusersfromdb")
    public String allUsersFromDB() {
        return usersService.findAllQUsersFromDB().toString();
    }
}

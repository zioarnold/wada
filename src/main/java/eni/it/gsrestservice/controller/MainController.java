package eni.it.gsrestservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainController {
    @Autowired
    private Environment environment;

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        return new ModelAndView("index");
    }
}

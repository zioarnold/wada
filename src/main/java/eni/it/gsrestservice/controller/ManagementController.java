package eni.it.gsrestservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ManagementController {
    @Autowired
    private Environment environment;
    @GetMapping("/managementPage")
    public ModelAndView managementPage(HttpServletRequest request) {
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        return new ModelAndView("/management");
    }
}

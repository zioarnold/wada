package eni.it.gsrestservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ManagementController {
    @GetMapping("/managementPage")
    public ModelAndView managementPage() {
        return new ModelAndView("/management");
    }
}

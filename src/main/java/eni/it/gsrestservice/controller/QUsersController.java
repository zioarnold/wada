package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.DBConnectionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@PropertySource(value = {"classpath:config.properties", "classpath:application.properties"})
@Configuration
public class QUsersController {
    private DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
    @Autowired
    private Environment environment;

    @GetMapping("/allqlikusersfromdb")
    public ModelAndView allQUsersFromDB(HttpServletRequest request) {
        initDB();
        request.setAttribute("qusers", dbConnectionOperation.getAllUsers());
        return new ModelAndView("allQUsersFromDB");
    }

    @RequestMapping("/searchquserondb")
    public ModelAndView searchQUserOnDB(HttpServletRequest request, @RequestParam(required = false, name = "quser_filter") String userId) {
        initDB();
        request.setAttribute("quser_filter", dbConnectionOperation.findQUser(userId));
        return new ModelAndView("searchQUserOnDB");
    }

    @RequestMapping(value = "/showUserType", method = RequestMethod.GET)
    public ModelAndView showUserType(HttpServletRequest request,
                                     @RequestParam(name = "quser") String userId) {
        initDB();
        request.setAttribute("other_data", dbConnectionOperation.findUserTypeByUserID(userId));
        return new ModelAndView("showUserType");
    }

    @GetMapping("/searchquserondbpage")
    public ModelAndView searchQUserOnDBPage() {
        initDB();
        return new ModelAndView("searchQUserOnDB");
    }

    public void initDB() {
        dbConnectionOperation.connectDB(environment.getProperty("db.hostname"),
                environment.getProperty("db.port"), environment.getProperty("db.sid"),
                environment.getProperty("db.username"), environment.getProperty("db.password"));
    }
}

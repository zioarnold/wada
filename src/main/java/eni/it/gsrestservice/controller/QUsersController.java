package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.DBConnectionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@PropertySource("classpath:application.properties")
public class QUsersController {
    private DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
    @Autowired
    private Environment environment;

    public void initDB() {
        dbConnectionOperation.initDB(
                environment.getProperty("db.hostname"),
                environment.getProperty("db.port"),
                environment.getProperty("db.sid"),
                environment.getProperty("db.username"),
                environment.getProperty("db.password"));
    }

    @GetMapping("/allqlikusersfromdb")
    public ModelAndView allQUsersFromDB(HttpServletRequest request) {
        initDB();
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        request.setAttribute("qusers", dbConnectionOperation.getAllUsers());
        return new ModelAndView("allQUsersFromDB");
    }

    @RequestMapping("/searchquserondb")
    public ModelAndView searchQUserOnDB(HttpServletRequest request, @RequestParam(required = false, name = "quser_filter") String userId) {
        initDB();
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        request.setAttribute("quser_filter", dbConnectionOperation.findQUser(userId));
        return new ModelAndView("searchQUserOnDB");
    }

    @RequestMapping(value = "/showUserType", method = RequestMethod.GET)
    public ModelAndView showUserType(HttpServletRequest request,
                                     @RequestParam(name = "quser") String userId) {
        initDB();
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        request.setAttribute("other_data", dbConnectionOperation.findUserTypeByUserID(userId));
        return new ModelAndView("showUserType");
    }

    @GetMapping("/searchquserondbpage")
    public ModelAndView searchQUserOnDBPage(HttpServletRequest request) {
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        return new ModelAndView("searchQUserOnDB");
    }
}

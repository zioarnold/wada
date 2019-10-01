package eni.it.gsrestservice.controller;

import com.google.gson.Gson;
import eni.it.gsrestservice.model.DBConnectionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        request.setAttribute("qusers", new Gson().toJson(dbConnectionOperation.getAllUsers()));
        return new ModelAndView("allQUsersFromDB");
    }

    @RequestMapping("/searchquserondb")
    public ModelAndView searchQUserOnDB(HttpServletRequest request, @RequestParam(required = false, name = "quser_filter") String userId) {
        initDB();
        request.setAttribute("quser_filter", new Gson().toJson(dbConnectionOperation.findQUser(userId)));
        return new ModelAndView("searchQUserOnDB");
    }

    @GetMapping("/showUserTypeGroup")
    public ModelAndView showUserType(HttpServletRequest request,
                                     @RequestParam(required = false, name = "showUserType") String showUserType,
                                     @RequestParam(required = false, name = "showUserGroup") String showUserGroup) {
        initDB();
        if (showUserType != null) {
            request.setAttribute("showUserType", new Gson().toJson(dbConnectionOperation.findUserTypeByUserID(showUserType)));
            return new ModelAndView("searchQUserOnDB");
        }
        if (showUserGroup != null) {
            request.setAttribute("showUserGroup", new Gson().toJson(dbConnectionOperation.findUserTypeByUserID(showUserGroup)));
            return new ModelAndView("searchQUserOnDB");
        }
        return new ModelAndView("searchQUserOnDB");
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

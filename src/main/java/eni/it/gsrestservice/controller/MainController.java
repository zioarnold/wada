package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.DBConnectionOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@PropertySource(value = {"classpath:config.properties", "classpath:application.properties"})
@Configuration
public class MainController {
    private DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
    @Autowired
    private Environment environment;

    @RequestMapping("/")
    public ModelAndView index() {
        initDB();
        return new ModelAndView("index");
    }

    public void initDB() {
        dbConnectionOperation.connectDB(environment.getProperty("db.hostname"),
                environment.getProperty("db.port"), environment.getProperty("db.sid"),
                environment.getProperty("db.username"), environment.getProperty("db.password"));
    }
}

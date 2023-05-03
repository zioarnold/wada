package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.Config;
import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.utility.Utility;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;

@RestController
public class LoginController {
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();

    @RequestMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        try {
            initDB();
            initQlikConnector();
            if (DBOracleOperations.isIsAuthenticated()) {
                if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("index")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
                    return new ModelAndView("index")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else {
                    return new ModelAndView("sessionExpired");
                }
            } else {
                return new ModelAndView("errorLogin").addObject("errorMsg",
                        ErrorWadaManagement.E_0015_NOT_AUTHENTICATED.getErrorMsg());
            }
        } catch (Exception e) {
            return new ModelAndView("errorLogin").addObject("errorMsg",
                    ErrorWadaManagement.E_500_INTERNAL_SERVER.getErrorMsg());
        }
    }

    @RequestMapping("/login")
    public ModelAndView loginPage(@RequestParam(required = false, name = "username") String username,
                                  @RequestParam(required = false, name = "password") String password) {
        initDB();
        if (dbOracleOperations.login(username, password)) {
            if (!QsAdminUsers.password.equals(Utility.MD5(password))) {
                return new ModelAndView("errorLogin")
                        .addObject("errorMsg", ErrorWadaManagement.E_0011_USERNAME_PASSWORD_INCORRECT.getErrorMsg());
            } else {
                return new ModelAndView("chooseFarm")
                        .addObject("farmList", dbOracleOperations.getAllFarms());
            }
        } else {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg());
        }
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        if (!dbOracleOperations.logout(QsAdminUsers.username)) {
            return new ModelAndView("logout");
        } else {
            return new ModelAndView("error").addObject("errorMsg",
                    ErrorWadaManagement.E_0014_UNABLE_TO_LOGOUT_USER.getErrorMsg());
        }
    }

    @RequestMapping("/createFarm")
    public ModelAndView createFarm() {
        return new ModelAndView("createFarm")
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @RequestMapping("/selectFarm")
    public ModelAndView selectFarm(@RequestParam(name = "farm") String farmName) {
        initDB();
        if (dbOracleOperations.selectFarm(farmName)) {
            if (dbOracleOperations.initConnector()) {
                initQlikConnector();
                return new ModelAndView("index")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("errorLogin")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg());
            }
        } else {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg());
        }
    }

    private void initQlikConnector() {
        qlikSenseConnector.initConnector(
                Farm.qsXrfKey,
                Farm.qsHost,
                Farm.qsPathClientJKS,
                Farm.qsPathRootJKS,
                Farm.qsKeyStorePwd,
                Farm.qsHeader,
                Farm.qsReloadTaskName);
        qlikSenseConnector.configureCertificate();
    }

    private void initDB() {
        String decodedPassword = new String(Base64.getUrlDecoder().decode(Config.dbPasswordMain));
        dbOracleOperations.initDB(
                Config.dbHostnameMain,
                Config.dbPortMain,
                Config.dbSidMain,
                Config.dbUsernameMain,
                decodedPassword,
                Config.dbQsAdminUsersTbl,
                Config.dbQsFarmsTbl
        );
    }
}

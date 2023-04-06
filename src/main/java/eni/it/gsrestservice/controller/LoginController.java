package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.LoggingMisc;
import eni.it.gsrestservice.model.DBConnectionOperationCentralized;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class LoginController {
    @Autowired
    private Environment environment;
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final DBConnectionOperationCentralized dbConnectionOperationCentralized = new DBConnectionOperationCentralized();
    private LoggingMisc loggingMisc; //oggetto di log

    @RequestMapping("/")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        try {
            initDB();
            if (initQlikConnector()) {
                if (DBConnectionOperationCentralized.isIsAuthenticated()) {
                    if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == 1) {
                        return new ModelAndView("index")
                                .addObject("farm_name", Farm.description)
                                .addObject("farm_environment", Farm.environment)
                                .addObject("ping_qlik", qlikSenseConnector.ping())
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    } else if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == -1) {
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
            } else {
                if (DBConnectionOperationCentralized.isIsAuthenticated()) {
                    if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == 1) {
                        return new ModelAndView("index")
                                .addObject("ping_qlik", 200)
                                .addObject("farm_name", "PIPPO")
                                .addObject("farm_environment", "DEV")
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    } else if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == -1) {
                        return new ModelAndView("index")
                                .addObject("ping_qlik", 200)
                                .addObject("farm_name", "PIPPO")
                                .addObject("farm_environment", "DEV")
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    } else {
                        return new ModelAndView("sessionExpired");
                    }
                } else {
                    return new ModelAndView("errorLogin").addObject("errorMsg",
                            ErrorWadaManagement.E_0015_NOT_AUTHENTICATED.getErrorMsg());
                }
            }
        } catch (Exception e) {
            return new ModelAndView("errorLogin").addObject("errorMsg",
                    ErrorWadaManagement.E_500_INTERNAL_SERVER.getErrorMsg());
        }
    }

    @RequestMapping("/login")
    public ModelAndView loginPage(@RequestParam(required = false, name = "username") String username,
                                  @RequestParam(required = false, name = "password") String password) throws IOException {
        initDB();
        if (dbConnectionOperationCentralized.login(username, password)) {
            if (!QsAdminUsers.password.equals(dbConnectionOperationCentralized.MD5(password))) {
                return new ModelAndView("errorLogin")
                        .addObject("errorMsg", ErrorWadaManagement.E_0011_USERNAME_PASSWORD_INCORRECT.getErrorMsg());
            } else {
//                return new ModelAndView("chooseFarm")
//                        .addObject("farmList", dbConnectionOperationCentralized.getAllFarms());
                return new ModelAndView("index")
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("user_logged_in", QsAdminUsers.username)
//                        .addObject("ping_qlik", qlikSenseConnector.ping())
//                        .addObject("farm_name", Farm.description)
//                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV");
            }
        } else {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg());
        }
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        if (!dbConnectionOperationCentralized.logout(QsAdminUsers.username)) {
            return new ModelAndView("logout");
        } else {
            return new ModelAndView("error").addObject("errorMsg",
                    ErrorWadaManagement.E_0014_UNABLE_TO_LOGOUT_USER.getErrorMsg());
        }
    }

    @RequestMapping("/selectFarm")
    public ModelAndView selectFarm(@RequestParam(name = "farm") String farmName) throws Exception {
        initDB();
        if (dbConnectionOperationCentralized.selectFarm(farmName)) {
            if (dbConnectionOperationCentralized.initConnector()) {
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

    private boolean initQlikConnector() {
        qlikSenseConnector.initConnector(
                Farm.qsXrfKey,
                Farm.qsHost,
                Farm.qsPathClientJKS,
                Farm.qsPathRootJKS,
                Farm.qsKeyStorePwd,
                Farm.qsHeader,
                Farm.qsReloadTaskName);
        return qlikSenseConnector.configureCertificate();
    }

    private void initDB() {
        dbConnectionOperationCentralized.initDB(
                environment.getProperty("db.hostname.main"),
                environment.getProperty("db.port.main"),
                environment.getProperty("db.sid.main"),
                environment.getProperty("db.username.main"),
                environment.getProperty("db.password.main"),
                environment.getProperty("db.qs.admin.users"),
                environment.getProperty("db.qs.farms")
        );
    }
}

package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController {
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();

    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    public ModelAndView addAdmin(@RequestParam(name = "username") String username,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "role") String role) {
        initQlikConnector();
        if (dbOracleOperations.createAdmin(username, password, role)) {
            return new ModelAndView("success")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("successMsg", "Utenza creata");
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0013_UNABLE_CREATE_ADMIN.getErrorMsg());
        }
    }

    @RequestMapping("/addAdminPage")
    public ModelAndView addAdminPage() {
        try {
            initQlikConnector();
            if (DBOracleOperations.isIsAuthenticated()) {
                if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("addAdminPage")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
                    return new ModelAndView("addAdminPage")
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
}

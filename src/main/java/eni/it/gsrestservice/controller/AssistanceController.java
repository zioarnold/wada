package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.Config;
import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;

@Controller
public class AssistanceController {
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();

    @GetMapping("/assistance")
    public ModelAndView assistance() {
        try {
            initDB();
            initQlikConnector();
            if (DBOracleOperations.isIsAuthenticated()) {
                if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("assistance")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
                    return new ModelAndView("assistance")
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

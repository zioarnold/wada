package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class QUsersController {
    private final DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
    private final DBConnectionOperationCentralized dbConnectionOperationCentralized = new DBConnectionOperationCentralized();
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    @Autowired
    private Environment environment;

    private void initDB() {
        dbConnectionOperation.initDB(
                Farm.dbHost,
                Farm.dbPort,
                Farm.dbSid,
                Farm.dbUser,
                Farm.dbPassword,
                environment.getProperty("db.tabuser"),
                environment.getProperty("db.tabattrib")
        );
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

    @GetMapping("/AllQLIKUsersFromDB")
    public ModelAndView allQUsersFromDB() throws Exception {
        initDB();
        initQlikConnector();
        if (dbConnectionOperation.getAllUsers().size() == 0) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0006_USERS_NOT_EXISTING_ON_DB.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("allQUsersFromDB")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("qusers", dbConnectionOperation.getAllUsers());
        }
    }

    @RequestMapping("/searchQUserOnDB")
    public ModelAndView searchQUserOnDB(@RequestParam(required = false, name = "quser_filter") String userId) throws Exception {
        initDB();
        initQlikConnector();
        if (dbConnectionOperation.findQUser(userId).size() == 0) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0010_USER_IS_NOT_ON_DB.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("searchQUserOnDB")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("quser_filter", dbConnectionOperation.findQUser(userId));
        }
    }

    @RequestMapping(value = "/showUserType", method = RequestMethod.GET)
    public ModelAndView showUserType(@RequestParam(name = "quser") String userId) throws Exception {
        initDB();
        initQlikConnector();
        if (dbConnectionOperation.findUserTypeByUserID(userId).size() == 0) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0010_USER_IS_NOT_ON_DB.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("showUserType")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("other_data", dbConnectionOperation.findUserTypeByUserID(userId));
        }
    }

    @GetMapping("/searchQUserOnDBPage")
    public ModelAndView searchQUserOnDBPage() throws Exception {
        initQlikConnector();
        return new ModelAndView("searchQUserOnDB")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseConnector.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @GetMapping(value = "/singleUploadPage")
    public ModelAndView singleUploadPage() {
        try {
            initDB();
            initQlikConnector();
            if (DBConnectionOperationCentralized.isIsAuthenticated()) {
                if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("singleUploadPage")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role)
                            .addObject("rolesList", new RolesListConfig()
                                    .initRolesList(environment.getProperty("roles.config.json.path")));
                } else if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == -1) {
                    return new ModelAndView("singleUploadPage")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role)
                            .addObject("rolesList", new RolesListConfig()
                                    .initRolesList(environment.getProperty("roles.config.json.path")));
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

    @RequestMapping(value = "/singleUpload")
    public ModelAndView singleUpload(@RequestParam(required = false, name = "userId") String userId,
                                     @RequestParam(required = false, name = "userRole") String userRole,
                                     @RequestParam(required = false, name = "userGroup") String userGroup) throws IOException {
        initDB();
        dbConnectionOperation.initFile(environment.getProperty("log.role.exist.for.user"));
        new LDAPConnector().searchOnLDAPInsertToDB(userId, userRole, userGroup);
        dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su questa farm: " +
                Farm.description + " di " + Farm.environment + " ha censito utente :" + userId.toUpperCase() + " con ruolo: "
                + userRole + " e con il gruppo: " + userGroup + "')");
        return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId);
    }

    private void initQlikConnector() throws Exception {
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

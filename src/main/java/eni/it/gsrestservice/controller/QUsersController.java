package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.db.DBPostgresOperations;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.LDAPConnector;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class QUsersController {
    private final DBPostgresOperations dbPostgresOperations = new DBPostgresOperations();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    @Autowired
    private Environment environment;

    private void initDB() {
        CSVReaderController.initAllDBPostgresOracle(dbPostgresOperations, environment, dbOracleOperations);
    }

    @GetMapping("/AllQLIKUsersFromDB")
    public ModelAndView allQUsersFromDB() throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbPostgresOperations.getAllUsers().size() == 0) {
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
                        .addObject("qusers", dbPostgresOperations.getAllUsers());
            }
        } else {
            if (dbPostgresOperations.getAllUsers().size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0006_USERS_NOT_EXISTING_ON_DB.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("allQUsersFromDB")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("qusers", dbPostgresOperations.getAllUsers());
            }
        }
    }

    @RequestMapping("/searchQUserOnDB")
    public ModelAndView searchQUserOnDB(@RequestParam(required = false, name = "quser_filter") String userId) throws Exception {
        initDB();
        initQlikConnector();
        if (dbPostgresOperations.findQUser(userId).size() == 0) {
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
                    .addObject("quser_filter", dbPostgresOperations.findQUser(userId));
        }
    }

    @RequestMapping(value = "/showUserType", method = RequestMethod.GET)
    public ModelAndView showUserType(@RequestParam(name = "quser") String userId) throws Exception {
        initDB();
        initQlikConnector();
        if (dbPostgresOperations.findUserTypeByUserID(userId).size() == 0) {
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
                    .addObject("other_data", dbPostgresOperations.findUserTypeByUserID(userId));
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
            if (initQlikConnector()) {
                if (DBOracleOperations.isIsAuthenticated()) {
                    if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                        return new ModelAndView("singleUploadPage")
                                .addObject("farm_name", Farm.description)
                                .addObject("farm_environment", Farm.environment)
                                .addObject("ping_qlik", qlikSenseConnector.ping())
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role)
                                .addObject("rolesList", new RolesListConfig()
                                        .initRolesList(environment.getProperty("roles.config.json.path")));
                    } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
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
            } else {
                if (DBOracleOperations.isIsAuthenticated()) {
                    if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                        return new ModelAndView("singleUploadPage")
                                .addObject("ping_qlik", 200)
                                .addObject("farm_name", "PIPPO")
                                .addObject("farm_environment", "DEV")
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role)
                                .addObject("rolesList", new RolesListConfig()
                                        .initRolesList(environment.getProperty("roles.config.json.path")));
                    } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
                        return new ModelAndView("singleUploadPage")
                                .addObject("ping_qlik", 200)
                                .addObject("farm_name", "PIPPO")
                                .addObject("farm_environment", "DEV")
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
        dbPostgresOperations.initFile(environment.getProperty("log.role.exist.for.user"));
        new LDAPConnector().searchOnLDAPInsertToDB(userId, userRole, userGroup);
        dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su questa farm: " +
                Farm.description + " di " + Farm.environment + " ha censito utente :" + userId.toUpperCase() + " con ruolo: "
                + userRole + " e con il gruppo: " + userGroup + "')");
        return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId);
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
}

package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Serializable;

@Controller
public class ManagementController implements Serializable {
    private final DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
    private final DBConnectionOperationCentralized dbConnectionOperationCentralized = new DBConnectionOperationCentralized();
    @Autowired
    private Environment environment;
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();

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
    }

    private boolean initQlikConnector() throws Exception {
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

    @GetMapping("/managementPage")
    public ModelAndView managementPage() {
        try {
            initDB();
            if (initQlikConnector()) {
                if (DBConnectionOperationCentralized.isIsAuthenticated()) {
                    if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == 1) {
                        if (dbConnectionOperation.getAllUsers().size() == 0) {
                            return new ModelAndView("error")
                                    .addObject("farm_name", Farm.description)
                                    .addObject("farm_environment", Farm.environment)
                                    .addObject("ping_qlik", qlikSenseConnector.ping())
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role);
                        } else {
                            return new ModelAndView("/management")
                                    .addObject("farm_name", Farm.description)
                                    .addObject("farm_environment", Farm.environment)
                                    .addObject("ping_qlik", qlikSenseConnector.ping())
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role)
                                    .addObject("qusers", dbConnectionOperation.getAllUsers())
                                    .addObject("rolesList", new RolesListConfig()
                                            .initRolesList(environment.getProperty("roles.config.json.path")));
                        }
                    } else if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == -1) {
                        if (dbConnectionOperation.getAllUsers().size() == 0) {
                            return new ModelAndView("error")
                                    .addObject("errorMsg", ErrorWadaManagement.E_0006_USERS_NOT_EXISTING_ON_DB.getErrorMsg())
                                    .addObject("farm_name", Farm.description)
                                    .addObject("farm_environment", Farm.environment)
                                    .addObject("ping_qlik", qlikSenseConnector.ping())
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role);
                        } else {
                            return new ModelAndView("/management")
                                    .addObject("farm_name", Farm.description)
                                    .addObject("farm_environment", Farm.environment)
                                    .addObject("ping_qlik", qlikSenseConnector.ping())
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role)
                                    .addObject("qusers", dbConnectionOperation.getAllUsers())
                                    .addObject("rolesList", new RolesListConfig()
                                            .initRolesList(environment.getProperty("roles.config.json.path")));
                        }
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
                        if (dbConnectionOperation.getAllUsers().size() == 0) {
                            return new ModelAndView("error")
                                    .addObject("ping_qlik", 200)
                                    .addObject("farm_name", "PIPPO")
                                    .addObject("farm_environment", "DEV")
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role);
                        } else {
                            return new ModelAndView("/management")
                                    .addObject("ping_qlik", 200)
                                    .addObject("farm_name", "PIPPO")
                                    .addObject("farm_environment", "DEV")
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role)
                                    .addObject("qusers", dbConnectionOperation.getAllUsers())
                                    .addObject("rolesList", new RolesListConfig()
                                            .initRolesList(environment.getProperty("roles.config.json.path")));
                        }
                    } else if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == -1) {
                        if (dbConnectionOperation.getAllUsers().size() == 0) {
                            return new ModelAndView("error")
                                    .addObject("errorMsg", ErrorWadaManagement.E_0006_USERS_NOT_EXISTING_ON_DB.getErrorMsg())
                                    .addObject("ping_qlik", 200)
                                    .addObject("farm_name", "PIPPO")
                                    .addObject("farm_environment", "DEV")
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role);
                        } else {
                            return new ModelAndView("/management")
                                    .addObject("ping_qlik", 200)
                                    .addObject("farm_name", "PIPPO")
                                    .addObject("farm_environment", "DEV")
                                    .addObject("user_logged_in", QsAdminUsers.username)
                                    .addObject("user_role_logged_in", QsAdminUsers.role)
                                    .addObject("qusers", dbConnectionOperation.getAllUsers())
                                    .addObject("rolesList", new RolesListConfig()
                                            .initRolesList(environment.getProperty("roles.config.json.path")));
                        }
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

    @RequestMapping("/managementPageShowUserData")
    public ModelAndView managementPageShowUserData(@RequestParam(required = false, name = "quser_filter") String userId) throws Exception {
        initDB();
        initQlikConnector();
        if (initQlikConnector()) {
            return new ModelAndView("/management")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("quser_filter", dbConnectionOperation.findUserRoleByUserID(userId));

        } else {
            return new ModelAndView("/management")
                    .addObject("ping_qlik", 200)
                    .addObject("farm_name", "PIPPO")
                    .addObject("farm_environment", "DEV")
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("quser_filter", dbConnectionOperation.findUserRoleByUserID(userId));
        }
    }

    @RequestMapping(value = "/managementPageEditTypeRole")
    public ModelAndView managementPageEditTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                   @RequestParam(required = false, name = "roleGroup") String roleGroup,
                                                   @RequestParam(required = false, name = "oldRole") String oldRole,
                                                   @RequestParam(required = false, name = "newUserRole") String userRole) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperation.updateRoleByUserID(userId, roleGroup, oldRole, userRole)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUE ('Utente "
                        + QsAdminUsers.username + " su questa farm: " + Farm.description + " di " +
                        Farm.environment + " ha eseguito questa query: update " +
                        environment.getProperty("db.tabattrib") + " set value = " + userRole +
                        " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase() + "')");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0005_USER_NOT_UPDATED.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperation.updateRoleByUserID(userId, roleGroup, oldRole, userRole)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUE ('Utente "
                        + QsAdminUsers.username + " su questa farm: " + Farm.description + " di " +
                        Farm.environment + " ha eseguito questa query: update " +
                        environment.getProperty("db.tabattrib") + " set value = " + userRole +
                        " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase() + "')");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0005_USER_NOT_UPDATED.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @RequestMapping(value = "/managementPageDeleteTypeRole")
    public ModelAndView managementPageDeleteTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                     @RequestParam(required = false, name = "type") String type,
                                                     @RequestParam(required = false, name = "value") String value) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperation.deleteRoleGroupByUserID(userId, type, value)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
                        + QsAdminUsers.username + " su questa farm: " + Farm.description + " di "
                        + Farm.environment + " ha eseguto la query: delete from " + environment.getProperty("db.tabattrib")
                        + " where userid like " + userId.toUpperCase() + " and type like " + type + " and value like " + value + "')");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0004_GROUP_OR_ROLE_NOT_DELETED.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperation.deleteRoleGroupByUserID(userId, type, value)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
                        + QsAdminUsers.username + " su questa farm: " + Farm.description + " di "
                        + Farm.environment + " ha eseguto la query: delete from " + environment.getProperty("db.tabattrib")
                        + " where userid like " + userId.toUpperCase() + " and type like " + type + " and value like " + value + "')");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0004_GROUP_OR_ROLE_NOT_DELETED.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @RequestMapping(value = "/executeReloadTaskQMC")
    public ModelAndView executeReloadTaskQMC() throws IOException {
        try {
            if (initQlikConnector()) {
                if (qlikSenseConnector.startReloadTask() == 201) {
                    return new ModelAndView("success")
                            .addObject("successMsg", "Comando inviato con successo")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else {
                    return new ModelAndView("error").addObject("errorMsg",
                                    ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("ping_qlik", 200)
                            .addObject("farm_name", "PIPPO")
                            .addObject("farm_environment", "DEV")
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                }
            } else {
                if (qlikSenseConnector.startReloadTask() == 201) {
                    return new ModelAndView("success")
                            .addObject("successMsg", "Comando inviato con successo")
                            .addObject("ping_qlik", 200)
                            .addObject("farm_name", "PIPPO")
                            .addObject("farm_environment", "DEV")
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else {
                    return new ModelAndView("error").addObject("errorMsg",
                                    ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                            .addObject("ping_qlik", 200)
                            .addObject("farm_name", "PIPPO")
                            .addObject("farm_environment", "DEV")
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                }
            }
        } catch (Exception e) {
            return new ModelAndView("error").addObject("errorMsg",
                            ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
//                    .addObject("farm_name", Farm.description)
//                    .addObject("farm_environment", Farm.environment)
//                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("ping_qlik", 200)
                    .addObject("farm_name", "PIPPO")
                    .addObject("farm_environment", "DEV")
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping(value = "/managementPageDelete")
    public ModelAndView managementPageDelete(@RequestParam(required = false, name = "userId") String userId) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperation.deleteUserID(userId)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
                        + QsAdminUsers.username + " su questa farm: " + Farm.description +
                        " di " + Farm.environment + " ha eseguito la query: delete from "
                        + environment.getProperty("db.tabuser") + " where userid like " + userId.toUpperCase() + "')");
                return new ModelAndView("redirect:/managementPage")
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0003_USER_NOT_DELETED.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperation.deleteUserID(userId)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
                        + QsAdminUsers.username + " su questa farm: " + Farm.description +
                        " di " + Farm.environment + " ha eseguito la query: delete from "
                        + environment.getProperty("db.tabuser") + " where userid like " + userId.toUpperCase() + "')");
                return new ModelAndView("redirect:/managementPage")
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0003_USER_NOT_DELETED.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @RequestMapping(value = "/report")
    public ModelAndView report() throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.report().size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0018_NO_REPORT_EXISTS.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("report")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("report", dbConnectionOperationCentralized.report());
            }
        } else {
            if (dbConnectionOperationCentralized.report().size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0018_NO_REPORT_EXISTS.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("report")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("report", dbConnectionOperationCentralized.report());
            }
        }
    }

    @RequestMapping(value = "/managementPageAddTypeRole")
    public ModelAndView managementPageAddTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "type") String type,
                                                  @RequestParam(required = false, name = "userGroup") String userGroup) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperation.insertIntoAttribGroup(userId, type, userGroup)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su questa farm: " +
                        Farm.description + " di " + Farm.environment + " ha eseguito questa query: INSERT INTO "
                        + environment.getProperty("db.tabattrib") + " (userid, type, value) VALUES("
                        + userId.toUpperCase() + "," + type + "," + userGroup + ")'");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0002_INSERT_GROUP_BY_USER_ID_FAILED.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperation.insertIntoAttribGroup(userId, type, userGroup)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su questa farm: " +
                        Farm.description + " di " + Farm.environment + " ha eseguito questa query: INSERT INTO "
                        + environment.getProperty("db.tabattrib") + " (userid, type, value) VALUES("
                        + userId.toUpperCase() + "," + type + "," + userGroup + ")'");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0002_INSERT_GROUP_BY_USER_ID_FAILED.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @RequestMapping(value = "/managementPageDisableUser")
    public ModelAndView managementPageDisableUser(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "disableYN") String disableYN) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperation.disableUserById(userId, disableYN)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES( 'Utenza " + QsAdminUsers.username + " su questa farm: "
                        + Farm.description + " di " + Farm.environment + " ha eseguto questa query: update "
                        + environment.getProperty("db.tabuser") + " set user_is_active = " + disableYN
                        + " where userid like " + userId.toUpperCase() + "')");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperation.disableUserById(userId, disableYN)) {
                dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES( 'Utenza " + QsAdminUsers.username + " su questa farm: "
                        + Farm.description + " di " + Farm.environment + " ha eseguto questa query: update "
                        + environment.getProperty("db.tabuser") + " set user_is_active = " + disableYN
                        + " where userid like " + userId.toUpperCase() + "')");
                return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                        .addObject("ping_qlik", qlikSenseConnector.ping());
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @RequestMapping(value = "/managementPageSyncTypeRole")
    public ModelAndView managementPageSyncTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                   @RequestParam(required = false, name = "oldRole") String oldRole) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (!getUserRoleByUserId(userId).equalsIgnoreCase("")) {
                if (getUserRoleByUserId(userId) != null) {
                    if (dbConnectionOperation.synchronizeUserRole(userId, oldRole, getUserRoleByUserId(userId))) {
                        dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su qesta farm: " +
                                Farm.description + " di " + Farm.environment + " ha eseguto questa query: update " +
                                environment.getProperty("db.tabattrib") + " set value = " + getUserRoleByUserId(userId) +
                                " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase() + "')");
                        return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                                .addObject("ping_qlik", qlikSenseConnector.ping());
                    } else {
                        return new ModelAndView("error")
                                .addObject("errorMsg", ErrorWadaManagement.E_0001_QMC_ROLE_VALUE_NULL.getErrorMsg())
                                .addObject("farm_name", Farm.description)
                                .addObject("farm_environment", Farm.environment)
                                .addObject("ping_qlik", qlikSenseConnector.ping())
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    }
                }
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0001_QMC_ROLE_VALUE_NULL.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            if (!getUserRoleByUserId(userId).equalsIgnoreCase("")) {
                if (getUserRoleByUserId(userId) != null) {
                    if (dbConnectionOperation.synchronizeUserRole(userId, oldRole, getUserRoleByUserId(userId))) {
                        dbConnectionOperationCentralized.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su qesta farm: " +
                                Farm.description + " di " + Farm.environment + " ha eseguto questa query: update " +
                                environment.getProperty("db.tabattrib") + " set value = " + getUserRoleByUserId(userId) +
                                " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase() + "')");
                        return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                                .addObject("ping_qlik", qlikSenseConnector.ping());
                    } else {
                        return new ModelAndView("error")
                                .addObject("errorMsg", ErrorWadaManagement.E_0001_QMC_ROLE_VALUE_NULL.getErrorMsg())
//                            .addObject("farm_name", Farm.description)
//                            .addObject("farm_environment", Farm.environment)
//                            .addObject("ping_qlik", qlikSenseConnector.ping())
                                .addObject("ping_qlik", 200)
                                .addObject("farm_name", "PIPPO")
                                .addObject("farm_environment", "DEV")
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    }
                }
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0001_QMC_ROLE_VALUE_NULL.getErrorMsg())
//                    .addObject("farm_name", Farm.description)
//                    .addObject("farm_environment", Farm.environment)
//                    .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
//                .addObject("farm_name", Farm.description)
//                .addObject("farm_environment", Farm.environment)
//                .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("ping_qlik", 200)
                    .addObject("farm_name", "PIPPO")
                    .addObject("farm_environment", "DEV")
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }

    }

    @GetMapping(value = "/addNewFarmPage")
    public ModelAndView addNewFarmPage() throws IOException {
        return new ModelAndView("addFarmPage")
//                .addObject("farm_name", Farm.description)
//                .addObject("farm_environment", Farm.environment)
//                .addObject("ping_qlik", qlikSenseConnector.ping())
                .addObject("ping_qlik", 200)
                .addObject("farm_name", "PIPPO")
                .addObject("farm_environment", "DEV")
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @RequestMapping(value = "/addNewFarm")
    public ModelAndView addNewFarm(@RequestParam(name = "description") String description,
                                   @RequestParam(name = "came") String came,
                                   @RequestParam(name = "dbUser") String dbUser,
                                   @RequestParam(name = "dbPassword") String dbPassword,
                                   @RequestParam(name = "dbHost") String dbHost,
                                   @RequestParam(name = "qsHost") String qsHost,
                                   @RequestParam(name = "qsPathClient") String qsPathClient,
                                   @RequestParam(name = "qsPathRoot") String qsPathRoot,
                                   @RequestParam(name = "qsXrfKey") String qsXrfKey,
                                   @RequestParam(name = "qsKsPassword") String qsKsPassword,
                                   @RequestParam(required = false, name = "note") String note,
                                   @RequestParam(name = "dbSid") String dbSid,
                                   @RequestParam(name = "dbPort") String dbPort,
                                   @RequestParam(name = "qsUserHeader") String qsUserHeader,
                                   @RequestParam(name = "environment") String environment) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.addNewFarm(description, dbUser, dbPassword, dbHost, qsHost,
                    qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment, came)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperationCentralized.addNewFarm(description, dbUser, dbPassword, dbHost, qsHost,
                    qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment, came)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @GetMapping("/allFarmPage")
    public ModelAndView allFarmPage() throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.getAllFarms().size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0017_NO_FARM_INSERTED.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("allFarmPage")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("all_farm", dbConnectionOperationCentralized.getAllFarms());
            }
        } else {
            if (dbConnectionOperationCentralized.getAllFarms().size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0017_NO_FARM_INSERTED.getErrorMsg())
//                    .addObject("farm_name", Farm.description)
//                    .addObject("farm_environment", Farm.environment)
//                    .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("allFarmPage")
//                    .addObject("farm_name", Farm.description)
//                    .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
//                    .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("all_farm", dbConnectionOperationCentralized.getAllFarms());
            }
        }
    }

    @RequestMapping("/allAdminsPage")
    public ModelAndView allAdminsPage() throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.getAllAdmins().size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("allAdminsPage")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("all_admins", dbConnectionOperationCentralized.getAllAdmins());
            }
        } else {
            if (dbConnectionOperationCentralized.getAllAdmins().size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("allAdminsPage")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("all_admins", dbConnectionOperationCentralized.getAllAdmins());
            }
        }
    }

    @GetMapping("/editFarm")
    public ModelAndView editFarm(@RequestParam(required = false, name = "farmId") String farmId) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.getFarmDataById(farmId).size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("editFarm")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("farm", dbConnectionOperationCentralized.getFarmDataById(farmId));
            }
        } else {
            if (dbConnectionOperationCentralized.getFarmDataById(farmId).size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("editFarm")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("farm", dbConnectionOperationCentralized.getFarmDataById(farmId));
            }
        }
    }

    @GetMapping("editAdmin")
    public ModelAndView editAdmin(@RequestParam(required = false, name = "adminId") String adminId) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.getAdminUserDataById(adminId).size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("editAdmin")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("user_admin", dbConnectionOperationCentralized.getAdminUserDataById(adminId));
            }
        } else {
            if (dbConnectionOperationCentralized.getAdminUserDataById(adminId).size() == 0) {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("editAdmin")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role)
                        .addObject("user_admin", dbConnectionOperationCentralized.getAdminUserDataById(adminId));
            }
        }
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@RequestParam(required = false, name = "adminId") String adminId,
                                      @RequestParam(required = false, name = "resetPwd") String password) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.resetPasswordByUserId(adminId, password)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperationCentralized.resetPasswordByUserId(adminId, password)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @RequestMapping("/deleteFarm")
    public ModelAndView deleteFarm(@RequestParam(required = false, name = "farmId") String farmId) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.deleteFarmById(farmId)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperationCentralized.deleteFarmById(farmId)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    @RequestMapping("/deleteAdmin")
    public ModelAndView deleteAdmin(@RequestParam(required = false, name = "adminId") int id) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.deleteAdminModerById(id)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperationCentralized.deleteAdminModerById(id)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }

    }

    @RequestMapping("/saveFarm")
    public ModelAndView saveFarm(@RequestParam(required = false, name = "farmId") String farmId,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "came") String came,
                                 @RequestParam(name = "dbUser") String dbUser,
                                 @RequestParam(name = "dbPassword") String dbPassword,
                                 @RequestParam(name = "dbHost") String dbHost,
                                 @RequestParam(name = "qsHost") String qsHost,
                                 @RequestParam(name = "qsReloadTaskName") String qsReloadTaskName,
                                 @RequestParam(name = "qsPathClientJKS") String qsPathClient,
                                 @RequestParam(name = "qsPathRootJKS") String qsPathRoot,
                                 @RequestParam(name = "qsXrfKey") String qsXrfKey,
                                 @RequestParam(name = "qsKeyStorePwd") String qsKsPassword,
                                 @RequestParam(required = false, name = "note") String note,
                                 @RequestParam(name = "dbSid") String dbSid,
                                 @RequestParam(name = "dbPort") String dbPort,
                                 @RequestParam(name = "qsHeader") String qsUserHeader,
                                 @RequestParam(name = "environment") String environment) throws Exception {
        initDB();
        if (initQlikConnector()) {
            if (dbConnectionOperationCentralized.updateFarm(farmId, description, dbUser, dbPassword, dbHost, qsHost,
                    qsReloadTaskName, qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword,
                    note, dbSid, dbPort, qsUserHeader, environment, came)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } else {
            if (dbConnectionOperationCentralized.updateFarm(farmId, description, dbUser, dbPassword, dbHost, qsHost,
                    qsReloadTaskName, qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword,
                    note, dbSid, dbPort, qsUserHeader, environment, came)) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Operazione e` andata a buon fine")
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                        .addObject("ping_qlik", 200)
                        .addObject("farm_name", "PIPPO")
                        .addObject("farm_environment", "DEV")
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        }
    }

    private String getUserRoleByUserId(String userId) throws Exception {
        return qlikSenseConnector.getUserRoleByUserId(userId);
    }
}

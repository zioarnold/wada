package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.Config;
import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.db.DBPostgresOperations;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Serializable;

@Controller
public class ManagementController implements Serializable {
    private final DBPostgresOperations dbPostgresOperations = new DBPostgresOperations();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final RolesListConfig rolesListConfig = new RolesListConfig();

    private void initDB() {
        dbPostgresOperations.initDB(
                Farm.dbHost,
                Farm.dbPort,
                Farm.dbSid,
                Farm.dbUser,
                Farm.dbPassword,
                Config.dbTabUsersTbl,
                Config.dbTabAttribTbl
        );
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

    @GetMapping("/managementPage")
    public ModelAndView managementPage() {
        try {
            initDB();
            initQlikConnector();
            if (DBOracleOperations.isIsAuthenticated()) {
                if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                    if (dbPostgresOperations.getAllUsers().size() == 0) {
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
                                .addObject("qusers", dbPostgresOperations.getAllUsers())
                                .addObject("rolesList", rolesListConfig.initRolesList(Config.rolesConfigJsonPath));
                    }
                } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
                    if (dbPostgresOperations.getAllUsers().size() == 0) {
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
                                .addObject("qusers", dbPostgresOperations.getAllUsers())
                                .addObject("rolesList", rolesListConfig.initRolesList(Config.rolesConfigJsonPath));
                    }
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

    @RequestMapping("/managementPageShowUserData")
    public ModelAndView managementPageShowUserData(@RequestParam(required = false, name = "quser_filter") String userId) {
        initDB();
        initQlikConnector();
        return new ModelAndView("/management")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseConnector.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role)
                .addObject("quser_filter", dbPostgresOperations.findUserRoleByUserID(userId));
    }

    @RequestMapping(value = "/managementPageEditTypeRole")
    public ModelAndView managementPageEditTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                   @RequestParam(required = false, name = "roleGroup") String roleGroup,
                                                   @RequestParam(required = false, name = "oldRole") String oldRole,
                                                   @RequestParam(required = false, name = "newUserRole") String userRole) {
        initDB();
        initQlikConnector();
        if (dbPostgresOperations.updateRoleByUserID(userId, roleGroup, oldRole, userRole)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUE ('Utente "
                    + QsAdminUsers.username + " su questa farm: " + Farm.description + " di " +
                    Farm.environment + " ha eseguito questa query: update " +
                    Config.dbTabAttribTbl + " set value = " + userRole +
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
    }

    @RequestMapping(value = "/managementPageDeleteTypeRole")
    public ModelAndView managementPageDeleteTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                     @RequestParam(required = false, name = "type") String type,
                                                     @RequestParam(required = false, name = "value") String value) {
        initDB();
        initQlikConnector();
        if (dbPostgresOperations.deleteRoleGroupByUserID(userId, type, value)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
                    + QsAdminUsers.username + " su questa farm: " + Farm.description + " di "
                    + Farm.environment + " ha eseguto la query: delete from " + Config.dbTabAttribTbl
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
    }

    @RequestMapping(value = "/executeReloadTaskQMC")
    public ModelAndView executeReloadTaskQMC() throws IOException {
        try {
            initQlikConnector();
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
        } catch (Exception e) {
            return new ModelAndView("error").addObject("errorMsg",
                            ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseConnector.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping(value = "/managementPageDelete")
    public ModelAndView managementPageDelete(@RequestParam(required = false, name = "userId") String userId) {
        initDB();
        initQlikConnector();
        if (dbPostgresOperations.deleteUserID(userId)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
                    + QsAdminUsers.username + " su questa farm: " + Farm.description +
                    " di " + Farm.environment + " ha eseguito la query: delete from "
                    + Config.dbTabUsersTbl + " where userid like " + userId.toUpperCase() + "')");
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
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView report() throws Exception {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.report().size() == 0) {
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
                    .addObject("report", dbOracleOperations.report());
        }
    }

    @RequestMapping(value = "/managementPageAddTypeRole")
    public ModelAndView managementPageAddTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "type") String type,
                                                  @RequestParam(required = false, name = "userGroup") String userGroup) {
        initDB();
        initQlikConnector();
        if (dbPostgresOperations.insertIntoAttribGroup(userId, type, userGroup)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su questa farm: " +
                    Farm.description + " di " + Farm.environment + " ha eseguito questa query: INSERT INTO "
                    + Config.dbTabAttribTbl + " (userid, type, value) VALUES("
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
    }

    @RequestMapping(value = "/managementPageDisableUser")
    public ModelAndView managementPageDisableUser(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "disableYN") String disableYN) {
        initDB();
        initQlikConnector();
        if (dbPostgresOperations.disableUserById(userId, disableYN)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES( 'Utenza " + QsAdminUsers.username + " su questa farm: "
                    + Farm.description + " di " + Farm.environment + " ha eseguto questa query: update "
                    + Config.dbTabUsersTbl + " set user_is_active = " + disableYN
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
    }

    @RequestMapping(value = "/managementPageSyncTypeRole")
    public ModelAndView managementPageSyncTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                   @RequestParam(required = false, name = "oldRole") String oldRole) throws Exception {
        initDB();
        initQlikConnector();
        if (!getUserRoleByUserId(userId).equalsIgnoreCase("")) {
            if (getUserRoleByUserId(userId) != null) {
                if (dbPostgresOperations.synchronizeUserRole(userId, oldRole, getUserRoleByUserId(userId))) {
                    dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su qesta farm: " +
                            Farm.description + " di " + Farm.environment + " ha eseguto questa query: update " +
                            Config.dbTabUsersTbl + " set value = " + getUserRoleByUserId(userId) +
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
    }

    @GetMapping(value = "/addNewFarmPage")
    public ModelAndView addNewFarmPage() {
        initQlikConnector();
        return new ModelAndView("addFarmPage")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseConnector.ping())
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
                                   @RequestParam(name = "environment") String environment) {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.addNewFarm(description, dbUser, dbPassword, dbHost, qsHost,
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
    }

    @RequestMapping(value = "/createFarmDB", method = RequestMethod.POST)
    public ModelAndView createFarmDB(@RequestParam(name = "description") String description,
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
                                     @RequestParam(name = "environment") String environment) {
        initDB();
        if (dbOracleOperations.addNewFarm(description, dbUser, dbPassword, dbHost, qsHost,
                qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment, came)) {
            return new ModelAndView("chooseFarm")
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("farmList", dbOracleOperations.getAllFarms());
        } else {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg());
        }
    }

    @GetMapping("/allFarmPage")
    public ModelAndView allFarmPage() throws Exception {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.getAllFarms().size() == 0) {
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
                    .addObject("all_farm", dbOracleOperations.getAllFarms());
        }
    }

    @RequestMapping("/allAdminsPage")
    public ModelAndView allAdminsPage() throws Exception {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.getAllAdmins().size() == 0) {
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
                    .addObject("all_admins", dbOracleOperations.getAllAdmins());
        }
    }

    @GetMapping("/editFarm")
    public ModelAndView editFarm(@RequestParam(required = false, name = "farmId") String farmId) {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.getFarmDataById(farmId).size() == 0) {
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
                    .addObject("farm", dbOracleOperations.getFarmDataById(farmId));
        }
    }

    @GetMapping("/editAdmin")
    public ModelAndView editAdmin(@RequestParam(required = false, name = "adminId") String adminId) {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.getAdminUserDataById(adminId).size() == 0) {
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
                    .addObject("user_admin", dbOracleOperations.getAdminUserDataById(adminId));
        }
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@RequestParam(required = false, name = "adminId") String adminId,
                                      @RequestParam(required = false, name = "resetPwd") String password) {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.resetPasswordByUserId(adminId, password)) {
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
    }

    @RequestMapping(value = "/deleteFarm", method = RequestMethod.POST)
    public ModelAndView deleteFarm(@RequestParam(required = false, name = "farmId") String farmId) {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.deleteFarmById(farmId)) {
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
    }

    @RequestMapping(value = "/deleteAdmin")
    public ModelAndView deleteAdmin(@RequestParam(required = false, name = "adminId") int id) {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.deleteAdminModerById(id)) {
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
    }

    @RequestMapping(value = "/saveFarm", method = RequestMethod.POST)
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
                                 @RequestParam(name = "environment") String environment) {
        initDB();
        initQlikConnector();
        if (dbOracleOperations.updateFarm(farmId, description, dbUser, dbPassword, dbHost, qsHost,
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
    }

    private String getUserRoleByUserId(String userId) throws Exception {
        return qlikSenseConnector.getUserRoleByUserId(userId);
    }
}

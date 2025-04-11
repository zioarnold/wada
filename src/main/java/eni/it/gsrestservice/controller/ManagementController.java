package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.db.DBPostgresOperations;
import eni.it.gsrestservice.entities.oracle.QsAdminUser;
import eni.it.gsrestservice.entities.oracle.QsAuditLog;
import eni.it.gsrestservice.entities.oracle.QsFarm;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.ora.QsAdminUsersService;
import eni.it.gsrestservice.service.ora.QsAuditLogService;
import eni.it.gsrestservice.service.ora.QsFarmService;
import eni.it.gsrestservice.service.post.QsUsersAttributesService;
import eni.it.gsrestservice.service.post.QsUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class ManagementController implements Serializable {
    private final QsFarmService qsFarmService;
    private final QsAdminUsersService qsAdminUsersService;
    private final QsAuditLogService qsAuditLogService;
    private final QsUsersService qsUsersService;
    private final QsUsersAttributesService qsUsersAttributesService;
    private final DBPostgresOperations dbPostgresOperations = new DBPostgresOperations();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();
    private final Environment environment;
    private final QlikSenseConnector qlikSenseConnector;
    private final RolesListConfig rolesListConfig;

    private void initDB() {
        dbPostgresOperations.initDB(
                Farm.dbHost,
                Farm.dbPort,
                Farm.dbSid,
                Farm.dbUser,
                Farm.dbPassword,
                environment.getProperty("db.tabuser"),
                environment.getProperty("db.tabattrib")
        );
    }

    @GetMapping("/managementPage")
    public ModelAndView managementPage() {
        try {
            if (qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
                if (qsAdminUsersService.checkSession(QsAdminUsers.username) == 1) {
                    if (qsUsersService.findAll().isEmpty()) {
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
                                .addObject("rolesList", rolesListConfig.getList());
                    }
                } else if (qsAdminUsersService.checkSession(QsAdminUsers.username) == -1) {
                    if (qsUsersService.findAll().isEmpty()) {
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
                                .addObject("rolesList", rolesListConfig.getList());
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

        if (qsUsersAttributesService.updateRoleByUserId(userId, roleGroup, oldRole, userRole) != null) {
            QsAuditLog qsAuditLog = new QsAuditLog();
            qsAuditLog.setDescription("Utente "
                                      + QsAdminUsers.username + " su questa farm: " + Farm.description + " di " +
                                      Farm.environment + " ha eseguito questa query: update " +
                                      environment.getProperty("db.tabattrib") + " set value = " + userRole +
                                      " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase());
            qsAuditLog.setExecutionData(LocalDate.now());
            qsAuditLogService.save(qsAuditLog);
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
        if (dbPostgresOperations.deleteRoleGroupByUserID(userId, type, value)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
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
    }

    @RequestMapping(value = "/executeReloadTaskQMC")
    public ModelAndView executeReloadTaskQMC() {
        try {
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
        if (dbPostgresOperations.deleteUserID(userId)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utente "
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
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ModelAndView report() throws Exception {
        List<QsAuditLog> report = qsAuditLogService.findAll();
        if (report.isEmpty()) {
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
                    .addObject("report", report);
        }
    }

    @RequestMapping(value = "/managementPageAddTypeRole")
    public ModelAndView managementPageAddTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "type") String type,
                                                  @RequestParam(required = false, name = "userGroup") String userGroup) {
        initDB();
        if (dbPostgresOperations.insertIntoAttribGroup(userId, type, userGroup)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su questa farm: " +
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
    }

    @RequestMapping(value = "/managementPageDisableUser")
    public ModelAndView managementPageDisableUser(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "disableYN") String disableYN) {
        initDB();
        if (dbPostgresOperations.disableUserById(userId, disableYN)) {
            dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES( 'Utenza " + QsAdminUsers.username + " su questa farm: "
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
    }

    @RequestMapping(value = "/managementPageSyncTypeRole")
    public ModelAndView managementPageSyncTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                   @RequestParam(required = false, name = "oldRole") String oldRole) throws Exception {
        initDB();
        if (!getUserRoleByUserId(userId).equalsIgnoreCase("")) {
            if (getUserRoleByUserId(userId) != null) {
                if (dbPostgresOperations.synchronizeUserRole(userId, oldRole, getUserRoleByUserId(userId))) {
                    dbOracleOperations.updateAudit("insert into QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " su qesta farm: " +
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
    }

    @GetMapping(value = "/addNewFarmPage")
    public ModelAndView addNewFarmPage() {
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
        QsFarm qsFarm = new QsFarm();
        qsFarm.setDescription(description);
        qsFarm.setCame(came);
        qsFarm.setDbuser(dbUser);
        qsFarm.setDbpassword(dbPassword);
        qsFarm.setDbhost(dbHost);
        qsFarm.setQshost(qsHost);
        qsFarm.setQspathclient(qsPathClient);
        qsFarm.setQspathroot(qsPathRoot);
        qsFarm.setQsxrfkey(qsXrfKey);
        qsFarm.setQskspasswd(qsKsPassword);
        qsFarm.setNote(note);
        qsFarm.setDbsid(dbSid);
        qsFarm.setDbport(dbPort);
        qsFarm.setQsuserheader(qsUserHeader);
        qsFarm.setEnvironment(environment);
        initDB();
        if (qsFarmService.create(qsFarm) != null) {
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
        QsFarm qsFarm = new QsFarm();
        qsFarm.setDescription(description);
        qsFarm.setCame(came);
        qsFarm.setDbuser(dbUser);
        qsFarm.setDbpassword(dbPassword);
        qsFarm.setDbhost(dbHost);
        qsFarm.setQshost(qsHost);
        qsFarm.setQspathclient(qsPathClient);
        qsFarm.setQspathroot(qsPathRoot);
        qsFarm.setQsxrfkey(qsXrfKey);
        qsFarm.setQskspasswd(qsKsPassword);
        qsFarm.setNote(note);
        qsFarm.setDbsid(dbSid);
        qsFarm.setDbport(dbPort);
        qsFarm.setQsuserheader(qsUserHeader);
        qsFarm.setEnvironment(environment);
        initDB();
        if (qsFarmService.create(qsFarm) != null) {
            return new ModelAndView("chooseFarm")
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("farmList", qsFarmService.findAllFarms());
        } else {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg());
        }
    }

    @GetMapping("/allFarmPage")
    public ModelAndView allFarmPage() throws Exception {
        initDB();
        if (qsFarmService.findAllFarms().isEmpty()) {
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
                    .addObject("all_farm", qsFarmService.findAllFarms());
        }
    }

    @RequestMapping("/allAdminsPage")
    public ModelAndView allAdminsPage() {
        initDB();
        if (qsAdminUsersService.findAllAdminUsers().isEmpty()) {
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
                    .addObject("all_admins", qsAdminUsersService.findAllAdminUsers());
        }
    }

    @GetMapping("/editFarm")
    public ModelAndView editFarm(@RequestParam(required = false, name = "farmId") Long farmId) {
        QsFarm qsFarm = qsFarmService.findById(farmId).orElseThrow(() -> new IllegalArgumentException("No farm found with id " + farmId));

        return new ModelAndView("editFarm")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseConnector.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role)
                .addObject("farm", new ArrayList<QsFarm>().add(qsFarm));
    }

    @GetMapping("/editAdmin")
    public ModelAndView editAdmin(@RequestParam(required = false, name = "adminId") Long adminId) {
        QsAdminUser qsAdminUser = qsAdminUsersService.findById(adminId).orElseThrow(() -> new NoSuchElementException("No admin found with id " + adminId));
        if (qsAdminUser == null) {
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
                    .addObject("user_admin", new ArrayList<QsAdminUser>().add(qsAdminUser));
        }
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@RequestParam(required = false, name = "adminId") String adminId,
                                      @RequestParam(required = false, name = "resetPwd") String password) {
        initDB();
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
    public ModelAndView deleteFarm(@RequestParam(required = false, name = "farmId") Long farmId) {
        qsFarmService.deleteById(farmId);
        return new ModelAndView("success")
                .addObject("successMsg", "Operazione e` andata a buon fine")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseConnector.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @RequestMapping(value = "/deleteAdmin")
    public ModelAndView deleteAdmin(@RequestParam(required = false, name = "adminId") Long id) {
        qsAdminUsersService.deleteById(id);
        return new ModelAndView("success")
                .addObject("successMsg", "Operazione e` andata a buon fine")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseConnector.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @RequestMapping(value = "/saveFarm", method = RequestMethod.POST)
    public ModelAndView saveFarm(@RequestParam(required = false, name = "farmId") Long farmId,
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

        QsFarm qsFarm = qsFarmService.findById(farmId)
                .orElseThrow(() -> new NoSuchElementException("No Qs Farm is found by: " + farmId + " to update."));

        qsFarm.setDescription(description);
        qsFarm.setCame(came);
        qsFarm.setDbuser(dbUser);
        qsFarm.setDbpassword(dbPassword);
        qsFarm.setDbhost(dbHost);
        qsFarm.setQshost(qsHost);
        qsFarm.setQspathclient(qsPathClient);
        qsFarm.setQspathroot(qsPathRoot);
        qsFarm.setQsxrfkey(qsXrfKey);
        qsFarm.setQskspasswd(qsKsPassword);
        qsFarm.setNote(note);
        qsFarm.setDbsid(dbSid);
        qsFarm.setDbport(dbPort);
        qsFarm.setQsuserheader(qsUserHeader);
        qsFarm.setEnvironment(environment);
        if (qsFarmService.update(qsFarm) != null) {
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

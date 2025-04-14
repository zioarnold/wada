package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.entities.postgres.QsAdminUser;
import eni.it.gsrestservice.entities.postgres.QsAuditLog;
import eni.it.gsrestservice.entities.postgres.QsFarm;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
public class ManagementController implements Serializable {
    private final QsFarmService qsFarmService;
    private final QsAdminUsersService qsAdminUsersService;
    private final QsAuditLogService qsAuditLogService;
    private final Environment environment;
    private final QsUsersService qsUsersService;
    private final QsUsersAttributesService qsUsersAttributesService;
    private final QlikSenseService qlikSenseService;
    private final RolesListConfig rolesListConfig;

    @Value("${db.db.tabattrib}")
    private String tabAttribute;

    @GetMapping("/managementPage")
    public ModelAndView managementPage() {
        try {
            if (qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
                if (qsAdminUsersService.checkSession(QsAdminUsers.username) == 1) {
                    if (qsUsersService.findAll().isEmpty()) {
                        return new ModelAndView("error")
                                .addObject("farm_name", Farm.description)
                                .addObject("farm_environment", Farm.environment)
                                .addObject("ping_qlik", qlikSenseService.ping())
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    } else {
                        return new ModelAndView("/management")
                                .addObject("farm_name", Farm.description)
                                .addObject("farm_environment", Farm.environment)
                                .addObject("ping_qlik", qlikSenseService.ping())
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role)
                                .addObject("qusers", qsUsersService.findAll())
                                .addObject("rolesList", rolesListConfig.initRolesList());
                    }
                } else if (qsAdminUsersService.checkSession(QsAdminUsers.username) == -1) {
                    if (qsUsersService.findAll().isEmpty()) {
                        return new ModelAndView("error")
                                .addObject("errorMsg", ErrorWadaManagement.E_0006_USERS_NOT_EXISTING_ON_DB.getErrorMsg())
                                .addObject("farm_name", Farm.description)
                                .addObject("farm_environment", Farm.environment)
                                .addObject("ping_qlik", qlikSenseService.ping())
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    } else {
                        return new ModelAndView("/management")
                                .addObject("farm_name", Farm.description)
                                .addObject("farm_environment", Farm.environment)
                                .addObject("ping_qlik", qlikSenseService.ping())
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role)
                                .addObject("qusers", qsUsersService.findAll())
                                .addObject("rolesList", rolesListConfig.initRolesList());
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
        return new ModelAndView("/management")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role)
                .addObject("quser_filter", qsUsersService.findUserRoleByUserID(userId));
    }

    @RequestMapping(value = "/managementPageEditTypeRole")
    public ModelAndView managementPageEditTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                   @RequestParam(required = false, name = "roleGroup") String roleGroup,
                                                   @RequestParam(required = false, name = "oldRole") String oldRole,
                                                   @RequestParam(required = false, name = "newUserRole") String newUserRole) {
        if (qsUsersAttributesService.updateRoleByUserId(userId, roleGroup, newUserRole)) {
            QsAuditLog qsAuditLog = new QsAuditLog();
            qsAuditLog.setDescription("Utente "
                                      + QsAdminUsers.username + " su questa farm: " + Farm.description + " di " +
                                      Farm.environment + " ha eseguito questa query: update " +
                                      tabAttribute + " set value = " + newUserRole +
                                      " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase());
            qsAuditLog.setExecutionData(OffsetDateTime.now());
            qsAuditLogService.save(qsAuditLog);
            return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                    .addObject("ping_qlik", qlikSenseService.ping());
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0005_USER_NOT_UPDATED.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping(value = "/managementPageDeleteTypeRole")
    public ModelAndView managementPageDeleteTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                     @RequestParam(required = false, name = "type") String type,
                                                     @RequestParam(required = false, name = "value") String value) {
        if (qsUsersAttributesService.deleteUserRoleByUserId(userId, type, value)) {
            QsAuditLog qsAuditLog = new QsAuditLog();
            qsAuditLog.setDescription("Utente "
                                      + QsAdminUsers.username + " su questa farm: " + Farm.description + " di "
                                      + Farm.environment + " ha eseguto la query: delete from " + tabAttribute
                                      + " where userid like " + userId.toUpperCase() + " and type like " + type + " and value like " + value);
            qsAuditLog.setExecutionData(OffsetDateTime.now());
            qsAuditLogService.save(qsAuditLog);
            return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                    .addObject("ping_qlik", qlikSenseService.ping());
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0004_GROUP_OR_ROLE_NOT_DELETED.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping(value = "/executeReloadTaskQMC")
    public ModelAndView executeReloadTaskQMC() {
        try {
            if (qlikSenseService.startReloadTask() == 201) {
                return new ModelAndView("success")
                        .addObject("successMsg", "Comando inviato con successo")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseService.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("error").addObject("errorMsg",
                                ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseService.ping())
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
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping(value = "/managementPageDelete")
    public ModelAndView managementPageDelete(@RequestParam(required = false, name = "userId") String userId) {
        if (qsUsersService.deleteById(userId)) {
            QsAuditLog qsAuditLog = new QsAuditLog();
            qsAuditLog.setDescription("Utente "
                                      + QsAdminUsers.username + " su questa farm: " + Farm.description +
                                      " di " + Farm.environment + " ha eseguito la query: delete from "
                                      + environment.getProperty("db.tabuser") + " where userid like " + userId.toUpperCase());
            qsAuditLog.setExecutionData(OffsetDateTime.now());
            qsAuditLogService.save(qsAuditLog);
            return new ModelAndView("redirect:/managementPage")
                    .addObject("ping_qlik", qlikSenseService.ping());
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0003_USER_NOT_DELETED.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
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
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("report")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("report", report);
        }
    }

    @RequestMapping(value = "/managementPageAddTypeRole")
    public ModelAndView managementPageAddTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "type") String type,
                                                  @RequestParam(required = false, name = "userGroup") String userGroup) {
        QsUsersAttrib qsUsersAttrib = new QsUsersAttrib();
        qsUsersAttrib.setUserId(qsUsersService.findByUserId(userId).getUserId());
        qsUsersAttrib.setType(type);
        qsUsersAttrib.setValue(userGroup);
        qsUsersAttributesService.create(qsUsersAttrib, tabAttribute);
        return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                .addObject("ping_qlik", qlikSenseService.ping());
    }

    @RequestMapping(value = "/managementPageDisableUser")
    public ModelAndView managementPageDisableUser(@RequestParam(required = false, name = "userId") String userId,
                                                  @RequestParam(required = false, name = "disableYN") String disableYN) {

        if (qsUsersService.disableUserById(userId, disableYN)) {
            QsAuditLog qsAuditLog = new QsAuditLog();
            qsAuditLog.setDescription("Utenza " + QsAdminUsers.username + " su questa farm: "
                                      + Farm.description + " di " + Farm.environment + " ha eseguto questa query: update "
                                      + environment.getProperty("db.tabuser") + " set user_is_active = " + disableYN
                                      + " where userid like " + userId.toUpperCase());
            qsAuditLog.setExecutionData(OffsetDateTime.now());
            qsAuditLogService.save(qsAuditLog);
            return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                    .addObject("ping_qlik", qlikSenseService.ping());
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping(value = "/managementPageSyncTypeRole")
    public ModelAndView managementPageSyncTypeRole(@RequestParam(required = false, name = "userId") String userId,
                                                   @RequestParam(required = false, name = "oldRole") String oldRole) throws Exception {
        String userRoleByUserId = qlikSenseService.getUserRoleByUserId(userId);
        if (!userRoleByUserId.equalsIgnoreCase("")) {
            if (!userRoleByUserId.equals("null")) {
                if (qsUsersAttributesService.updateUserRole(userId, oldRole, userRoleByUserId, tabAttribute)) {

                    return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId)
                            .addObject("ping_qlik", qlikSenseService.ping());
                } else {
                    return new ModelAndView("error")
                            .addObject("errorMsg", ErrorWadaManagement.E_0001_QMC_ROLE_VALUE_NULL.getErrorMsg())
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseService.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                }
            }
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0001_QMC_ROLE_VALUE_NULL.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
        return new ModelAndView("error")
                .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @GetMapping(value = "/addNewFarmPage")
    public ModelAndView addNewFarmPage() {
        return new ModelAndView("addFarmPage")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
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

        QsFarm qsFarm = ex(description, came, dbUser, dbPassword, dbHost, qsHost, qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment);

        if (qsFarmService.create(qsFarm) != null) {
            return new ModelAndView("success")
                    .addObject("successMsg", "Operazione e` andata a buon fine")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping("/createFarm")
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
        QsFarm qsFarm = ex(description, came, dbUser, dbPassword, dbHost, qsHost, qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment);
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
    public ModelAndView allFarmPage() {

        if (qsFarmService.findAllFarms().isEmpty()) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0017_NO_FARM_INSERTED.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("allFarmPage")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("all_farm", qsFarmService.findAllFarms());
        }
    }

    @RequestMapping("/allAdminsPage")
    public ModelAndView allAdminsPage() {

        if (qsAdminUsersService.findAllAdminUsers().isEmpty()) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("allAdminsPage")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("all_admins", qsAdminUsersService.findAllAdminUsers());
        }
    }

    @GetMapping("/editFarm")
    public ModelAndView editFarm(@RequestParam(required = false, name = "farmId") String farmId) {
        QsFarm qsFarm = qsFarmService.findById(farmId).orElseThrow(() -> new IllegalArgumentException("No farm found with id " + farmId));
        return new ModelAndView("editFarm")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role)
                .addObject("farm", qsFarm);
    }

    @GetMapping("/editAdmin")
    public ModelAndView editAdmin(@RequestParam(required = false, name = "adminId") Long adminId) {
        QsAdminUser qsAdminUser = qsAdminUsersService.findById(adminId).orElseThrow(() -> new NoSuchElementException("No admin found with id " + adminId));
        if (qsAdminUser == null) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("editAdmin")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("user_admin", qsAdminUser);
        }
    }

    @PostMapping("/resetPassword")
    public ModelAndView resetPassword(@RequestParam(required = false, name = "adminId") Long adminId,
                                      @RequestParam(required = false, name = "resetPwd") String password) {
        if (qsAdminUsersService.resetPassword(adminId, password)) {
            return new ModelAndView("success")
                    .addObject("successMsg", "Operazione e` andata a buon fine")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0016_USER_NOT_EXISTS.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    @RequestMapping(value = "/deleteFarm", method = RequestMethod.POST)
    public ModelAndView deleteFarm(@RequestParam(required = false, name = "farmId") String farmId) {
        qsFarmService.deleteById(farmId);
        return new ModelAndView("success")
                .addObject("successMsg", "Operazione e` andata a buon fine")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
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
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
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
        QsFarm qsFarm = updateFarm(farmId, description, came, dbUser, dbPassword, dbHost, qsHost, qsReloadTaskName, qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment);
        if (qsFarmService.update(qsFarm) != null) {
            return new ModelAndView("success")
                    .addObject("successMsg", "Operazione e` andata a buon fine")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_9999_UNKNOWN_ERROR.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        }
    }

    private QsFarm ex(@RequestParam(name = "description") String description,
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
        qsFarm.setDbUser(dbUser);
        qsFarm.setDbPassword(dbPassword);
        qsFarm.setDbHost(dbHost);
        qsFarm.setQsHost(qsHost);
        return commonQsData(qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment, qsFarm);
    }

    private QsFarm commonQsData(@RequestParam(name = "qsPathClient") String qsPathClient, @RequestParam(name = "qsPathRoot") String qsPathRoot, @RequestParam(name = "qsXrfKey") String qsXrfKey, @RequestParam(name = "qsKsPassword") String qsKsPassword, @RequestParam(required = false, name = "note") String note, @RequestParam(name = "dbSid") String dbSid, @RequestParam(name = "dbPort") String dbPort, @RequestParam(name = "qsUserHeader") String qsUserHeader, @RequestParam(name = "environment") String environment, QsFarm qsFarm) {
        qsFarm.setQsPathClient(qsPathClient);
        qsFarm.setQsPathRoot(qsPathRoot);
        qsFarm.setQsXrfKey(qsXrfKey);
        qsFarm.setQsKsPasswd(qsKsPassword);
        qsFarm.setNote(note);
        qsFarm.setDbSid(dbSid);
        qsFarm.setDbPort(dbPort);
        qsFarm.setQsUserHeader(qsUserHeader);
        qsFarm.setEnvironment(environment);
        return qsFarm;
    }

    private QsFarm updateFarm(@RequestParam(required = false, name = "farmId") String farmId,
                              @RequestParam(name = "description") String description,
                              @RequestParam(name = "came") String came,
                              @RequestParam(name = "dbUser") String dbUser,
                              @RequestParam(name = "dbPassword") String dbPassword,
                              @RequestParam(name = "dbHost") String dbHost,
                              @RequestParam(name = "qsHost") String qsHost,
                              @RequestParam(name = "qsReloadTaskName") String qsReloadTaskName,
                              @RequestParam(name = "qsPathClient") String qsPathClient,
                              @RequestParam(name = "qsPathRoot") String qsPathRoot,
                              @RequestParam(name = "qsXrfKey") String qsXrfKey,
                              @RequestParam(name = "qsKsPassword") String qsKsPassword,
                              @RequestParam(required = false, name = "note") String note,
                              @RequestParam(name = "dbSid") String dbSid,
                              @RequestParam(name = "dbPort") String dbPort,
                              @RequestParam(name = "qsUserHeader") String qsUserHeader,
                              @RequestParam(name = "environment") String environment) {
        QsFarm qsFarm = qsFarmService.findById(farmId).orElseThrow(() -> new IllegalArgumentException("No farm found with id " + farmId));
        qsFarm.setDescription(description);
        qsFarm.setCame(came);
        qsFarm.setDbUser(dbUser);
        qsFarm.setDbPassword(dbPassword);
        qsFarm.setDbHost(dbHost);
        qsFarm.setQsHost(qsHost);
        qsFarm.setQsReloadTaskName(qsReloadTaskName);
        return commonQsData(qsPathClient, qsPathRoot, qsXrfKey, qsKsPassword, note, dbSid, dbPort, qsUserHeader, environment, qsFarm);
    }
}

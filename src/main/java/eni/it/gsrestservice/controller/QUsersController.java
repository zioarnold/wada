package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.entities.oracle.QsAuditLog;
import eni.it.gsrestservice.entities.postgres.QsUser;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.LDAPConnector;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.ora.QsAdminUsersService;
import eni.it.gsrestservice.service.ora.QsAuditLogService;
import eni.it.gsrestservice.service.post.QsUsersAttributesService;
import eni.it.gsrestservice.service.post.QsUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QUsersController {
    private final QlikSenseConnector qlikSenseConnector;
    private final RolesListConfig rolesListConfig;
    private final QsUsersAttributesService qsUsersAttributesService;
    private final QsUsersService qsUsersService;
    private final QsAdminUsersService qsAdminUsersService;
    private final QsAuditLogService qsAuditLogService;


    @GetMapping("/AllQLIKUsersFromDB")
    public ModelAndView allQUsersFromDB() {
        List<QsUser> qsUsers = qsUsersService.findAll();
        if (qsUsers.isEmpty()) {
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
                    .addObject("qusers", qsUsers);
        }
    }

    @RequestMapping("/searchQUserOnDB")
    public ModelAndView searchQUserOnDB(@RequestParam(required = false, name = "quser_filter") Long userId) {
        if (qsUsersService.findById(userId) == null) {
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
                    .addObject("quser_filter", qsUsersService.findById(userId));
        }
    }

    @RequestMapping(value = "/showUserType", method = RequestMethod.GET)
    public ModelAndView showUserType(@RequestParam(name = "quser") String userId) {
        List<QsUsersAttrib> userTypeByUserId = qsUsersAttributesService.findUserTypeByUserId(userId);
        if (userTypeByUserId.isEmpty()) {
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
                    .addObject("other_data", userTypeByUserId);
        }
    }

    @GetMapping("/searchQUserOnDBPage")
    public ModelAndView searchQUserOnDBPage() {
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
            if (qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
                if (qsAdminUsersService.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("singleUploadPage")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role)
                            .addObject("rolesList", rolesListConfig.getList());
                } else if (qsAdminUsersService.checkSession(QsAdminUsers.username) == -1) {
                    return new ModelAndView("singleUploadPage")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role)
                            .addObject("rolesList", rolesListConfig.getList());
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

    @RequestMapping(value = "/singleUpload", method = RequestMethod.POST)
    public ModelAndView singleUpload(@RequestParam(required = false, name = "userId") String userId,
                                     @RequestParam(required = false, name = "userRole") String userRole,
                                     @RequestParam(required = false, name = "userGroup") String userGroup) throws IOException {
        new LDAPConnector().searchOnLDAPInsertToDB(userId, userRole, userGroup);
        QsAuditLog qsAuditLog = new QsAuditLog();
        qsAuditLog.setDescription("Utenza " + QsAdminUsers.username + " su questa farm: " +
                                  Farm.description + " di " + Farm.environment + " ha censito utente :" + userId.toUpperCase() + " con ruolo: "
                                  + userRole + " e con il gruppo: " + userGroup);
        qsAuditLog.setExecutionData(LocalDate.now());
        qsAuditLogService.save(qsAuditLog);
        return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId);
    }
}

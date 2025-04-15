package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.entities.postgres.QsAuditLog;
import eni.it.gsrestservice.entities.postgres.QsUser;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.LDAPService;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.post.QsAdminUsersService;
import eni.it.gsrestservice.service.post.QsAuditLogService;
import eni.it.gsrestservice.service.post.QsUsersAttributesService;
import eni.it.gsrestservice.service.post.QsUsersService;
import lombok.RequiredArgsConstructor;
import lombok.var;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class QUsersController {
    private static final String FARM_NAME = "farm_name";
    private static final String FARM_ENVIRONMENT = "farm_environment";
    private static final String PING_QLIK = "ping_qlik";
    private static final String USER_LOGGED = "user_logged_in";
    private static final String USER_ROLE = "user_role_logged_in";

    private final QlikSenseService qlikSenseService;
    private final RolesListConfig rolesListConfig;
    private final QsUsersAttributesService qsUsersAttributesService;
    private final QsUsersService qsUsersService;
    private final QsAdminUsersService qsAdminUsersService;
    private final QsAuditLogService qsAuditLogService;
    private final LDAPService ldapService;

    private ModelAndView addCommonAttributes(ModelAndView modelAndView) {
        return modelAndView
                .addObject(FARM_NAME, Farm.description)
                .addObject(FARM_ENVIRONMENT, Farm.environment)
                .addObject(PING_QLIK, qlikSenseService.ping())
                .addObject(USER_LOGGED, QsAdminUsers.username)
                .addObject(USER_ROLE, QsAdminUsers.role);
    }

    private ModelAndView createErrorView(String errorMsg) {
        return addCommonAttributes(new ModelAndView("error")
                .addObject("errorMsg", errorMsg));
    }


    @GetMapping("/AllQLIKUsersFromDB")
    public ModelAndView allQUsersFromDB() {
        List<QsUser> qsUsers = qsUsersService.findAll();
        if (qsUsers.isEmpty()) {
            return createErrorView(ErrorWadaManagement.E_0006_USERS_NOT_EXISTING_ON_DB.getErrorMsg());
        }
        return addCommonAttributes(new ModelAndView("allQUsersFromDB"))
                .addObject("qusers", qsUsers);
    }

    @RequestMapping("/searchUserOnLDAP")
    public ModelAndView searchUserOnLDAP(@RequestParam(required = false, name = "userID") String userId) throws IOException {
        var ldapResults = ldapService.searchOnLDAP(userId);
        if (ldapResults.isEmpty()) {
            return createErrorView(ErrorWadaManagement.E_0009_USER_NOT_IN_LDAP.getErrorMsg());
        }
        return addCommonAttributes(new ModelAndView("searchUserOnLDAP"))
                .addObject("userIDDATA", ldapResults);
    }

    @RequestMapping("/searchQUserOnDB")
    public ModelAndView searchQUserOnDB(@RequestParam(required = false, name = "quser_filter") String userId) {
        if (qsUsersService.findByUserId(userId) == null) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0010_USER_IS_NOT_ON_DB.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("searchQUserOnDB")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("quser_filter", qsUsersService.findByUserId(userId))
                    .addObject("other_data", qsUsersAttributesService.findUserTypeByUserId(userId));
        }
    }

    @RequestMapping(value = "/showUserType", method = RequestMethod.GET)
    public ModelAndView showUserType(@RequestParam(name = "quser") String userId) {
        QsUsersAttrib userTypeByUserId = qsUsersAttributesService.findUserTypeByUserId(userId);
        if (userTypeByUserId == null) {
            return new ModelAndView("error")
                    .addObject("errorMsg", ErrorWadaManagement.E_0010_USER_IS_NOT_ON_DB.getErrorMsg())
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role);
        } else {
            return new ModelAndView("showUserType")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
                    .addObject("user_logged_in", QsAdminUsers.username)
                    .addObject("user_role_logged_in", QsAdminUsers.role)
                    .addObject("other_data", userTypeByUserId);
        }
    }

    @GetMapping("/allUsersFromLDAP")
    public ModelAndView allUsersFromLDAP() {
        return new ModelAndView("allUsersFromLDAP")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role)
                .addObject("ldapusers", ldapService.findAll());
    }

    @GetMapping("/searchQUserOnDBPage")
    public ModelAndView searchQUserOnDBPage() {
        return new ModelAndView("searchQUserOnDB")
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @GetMapping(value = "/singleUploadPage")
    public ModelAndView singleUploadPage() {
        try {
            if (!qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
                return new ModelAndView("errorLogin")
                        .addObject("errorMsg", ErrorWadaManagement.E_0015_NOT_AUTHENTICATED.getErrorMsg());
            }

            int sessionStatus = qsAdminUsersService.checkSession(QsAdminUsers.username);
            if (sessionStatus == 0) {
                return new ModelAndView("sessionExpired");
            }

            return addCommonAttributes(new ModelAndView("singleUploadPage"))
                    .addObject("rolesList", rolesListConfig.initRolesList());

        } catch (Exception e) {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_500_INTERNAL_SERVER.getErrorMsg());
        }
    }

    @RequestMapping(value = "/singleUpload", method = RequestMethod.POST)
    public ModelAndView singleUpload(@RequestParam(required = false, name = "userId") String userId,
                                     @RequestParam(required = false, name = "userRole") String userRole,
                                     @RequestParam(required = false, name = "userGroup") String userGroup) throws IOException {
        ldapService.searchOnLDAPInsertToDB(userId, userRole, userGroup);
        QsAuditLog qsAuditLog = new QsAuditLog();
        qsAuditLog.setDescription("Utenza " + QsAdminUsers.username + " su questa farm: " +
                                  Farm.description + " di " + Farm.environment + " ha censito utente :" + userId.toUpperCase() + " con ruolo: "
                                  + userRole + " e con il gruppo: " + userGroup);
        qsAuditLog.setExecutionData(OffsetDateTime.now());
        qsAuditLogService.save(qsAuditLog);
        return new ModelAndView("redirect:/managementPageShowUserData?quser_filter=" + userId);
    }
}

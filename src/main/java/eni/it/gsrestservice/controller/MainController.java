package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.entities.oracle.QsAdminUser;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.ora.QsAdminUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static eni.it.gsrestservice.utility.Utility.MD5;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final QsAdminUsersService qsAdminUsersService;
    private final QlikSenseService qlikSenseService = new QlikSenseService();

    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    public ModelAndView addAdmin(@RequestParam(name = "username") String username,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "role") String role) {

        QsAdminUser qsAdminUsers = new QsAdminUser();
        qsAdminUsers.setUsername(username);
        qsAdminUsers.setPassword(MD5(password));
        qsAdminUsers.setRole(role);

        initQlikConnector();
        if (qsAdminUsersService.create(qsAdminUsers) != null) {
            return new ModelAndView("success")
                    .addObject("farm_name", Farm.description)
                    .addObject("farm_environment", Farm.environment)
                    .addObject("ping_qlik", qlikSenseService.ping())
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
            if (qsAdminUsersService.isAuthenticated(QsAdminUsers.username) || qsAdminUsersService.checkSession(QsAdminUsers.username) == -1) {
                return new ModelAndView("addAdminPage")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseService.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else if (qsAdminUsersService.isAuthenticated(QsAdminUsers.username) || qsAdminUsersService.checkSession(QsAdminUsers.username) == 1) {
                return new ModelAndView("addAdminPage")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseService.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
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
        QlikSenseService.initConnector(
                Farm.qsXrfKey,
                Farm.qsHost,
                Farm.qsPathClientJKS,
                Farm.qsPathRootJKS,
                Farm.qsKeyStorePwd,
                Farm.qsHeader,
                Farm.qsReloadTaskName);
        QlikSenseService.configureCertificate();
    }
}

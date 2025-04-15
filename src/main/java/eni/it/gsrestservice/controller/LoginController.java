package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.post.QsAdminUsersService;
import eni.it.gsrestservice.service.post.QsFarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static eni.it.gsrestservice.utility.Utility.MD5;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final QsAdminUsersService qsAdminUsersService;
    private final QlikSenseService qlikSenseService;
    private final QsFarmService qsFarmService;

    @GetMapping("/")
    public ModelAndView root() {
        return new ModelAndView("login");
    }


    @RequestMapping("/index")
    public ModelAndView index() {
        try {
            if (qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
                if (qsAdminUsersService.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("index")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseService.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else if (qsAdminUsersService.checkSession(QsAdminUsers.username) == -1) {
                    return new ModelAndView("index")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseService.ping())
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

    @RequestMapping(value = "/loginPage")
    public ModelAndView loginPage(@RequestParam(required = false, name = "username") String username,
                                  @RequestParam(required = false, name = "password") String password) {
        if (qsAdminUsersService.login(username, MD5(password)) != null) {
            return new ModelAndView("chooseFarm")
                    .addObject("farmList", qsFarmService.findAllFarms());
        } else {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_0011_USERNAME_PASSWORD_INCORRECT.getErrorMsg());
        }
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        if (!qsAdminUsersService.logout(QsAdminUsers.username)) {
            return new ModelAndView("logout");
        } else {
            return new ModelAndView("error").addObject("errorMsg",
                    ErrorWadaManagement.E_0014_UNABLE_TO_LOGOUT_USER.getErrorMsg());
        }
    }
}

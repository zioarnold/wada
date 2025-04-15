package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.post.QsAdminUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class UserGuideController {
    private static final String USER_GUIDE_VIEW = "userGuide";
    private static final String SESSION_EXPIRED_VIEW = "sessionExpired";
    private static final String ERROR_LOGIN_VIEW = "errorLogin";
    private final QlikSenseService qlikSenseService;
    private final QsAdminUsersService qsAdminUsersService;
    @GetMapping("/userGuide")
    public ModelAndView userGuide() {
        try {
            if (!qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
                return createErrorView(ErrorWadaManagement.E_0015_NOT_AUTHENTICATED.getErrorMsg());
            }

            int sessionStatus = qsAdminUsersService.checkSession(QsAdminUsers.username);
            if (sessionStatus == 0) {
                return new ModelAndView(SESSION_EXPIRED_VIEW);
            }

            return createUserGuideView();

        } catch (Exception e) {
            return createErrorView(ErrorWadaManagement.E_500_INTERNAL_SERVER.getErrorMsg());
        }
    }

    private ModelAndView createUserGuideView() {
        return new ModelAndView(USER_GUIDE_VIEW)
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    private ModelAndView createErrorView(String errorMessage) {
        return new ModelAndView(ERROR_LOGIN_VIEW)
                .addObject("errorMsg", errorMessage);
    }
}

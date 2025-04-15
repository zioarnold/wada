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
public class AssistanceController {

    private final QsAdminUsersService qsAdminUsersService;
    private final QlikSenseService qlikSenseService;

    private static final String VIEW_ASSISTANCE = "assistance";
    private static final String VIEW_SESSION_EXPIRED = "sessionExpired";
    private static final String VIEW_ERROR_LOGIN = "errorLogin";

    @GetMapping("/assistance")
    public ModelAndView assistance() {
        try {
            if (!qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
                return createErrorView(ErrorWadaManagement.E_0015_NOT_AUTHENTICATED.getErrorMsg());
            }

            int sessionCheck = qsAdminUsersService.checkSession(QsAdminUsers.username);
            if (sessionCheck == 0) {
                return new ModelAndView(VIEW_SESSION_EXPIRED);
            }

            return createAssistanceView();

        } catch (Exception e) {
            return createErrorView(ErrorWadaManagement.E_500_INTERNAL_SERVER.getErrorMsg());
        }
    }

    private ModelAndView createAssistanceView() {
        return new ModelAndView(VIEW_ASSISTANCE)
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    private ModelAndView createErrorView(String errorMsg) {
        return new ModelAndView(VIEW_ERROR_LOGIN)
                .addObject("errorMsg", errorMsg);
    }
}

package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.CSVReaderService;
import eni.it.gsrestservice.service.LDAPService;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.post.QsAdminUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class CSVReaderController {
    private static final String VIEW_MASSIVE_UPLOAD = "massiveUploadPage";
    private static final String VIEW_SESSION_EXPIRED = "sessionExpired";
    private static final String VIEW_ERROR_LOGIN = "errorLogin";
    private static final String VIEW_UPLOAD_SUCCESS = "uploadSuccess";
    private static final String VIEW_ERROR = "error";
    private final CSVReaderService csvReaderService;
    private final QlikSenseService qlikSenseService;

    private final QsAdminUsersService qsAdminUsersService;

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage() {
        if (!isUserAuthenticated()) {
            return createErrorLoginView(ErrorWadaManagement.E_0015_NOT_AUTHENTICATED.getErrorMsg());
        }

        if (!isValidSession()) {
            return new ModelAndView(VIEW_SESSION_EXPIRED);
        }

        return createBaseModelAndView(VIEW_MASSIVE_UPLOAD);
    }

    private boolean isUserAuthenticated() {
        return qsAdminUsersService.isAuthenticated(QsAdminUsers.username);
    }

    private boolean isValidSession() {
        int sessionStatus = qsAdminUsersService.checkSession(QsAdminUsers.username);
        return sessionStatus == 1 || sessionStatus == -1;
    }

    private ModelAndView createBaseModelAndView(String viewName) {
        return new ModelAndView(viewName)
                .addObject("farm_name", Farm.description)
                .addObject("farm_environment", Farm.environment)
                .addObject("ping_qlik", qlikSenseService.ping())
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    private ModelAndView createErrorLoginView(String errorMsg) {
        return new ModelAndView(VIEW_ERROR_LOGIN).addObject("errorMsg", errorMsg);
    }

    @PostMapping("/massiveUpload")
    public ModelAndView massiveUpload(@RequestParam("file") MultipartFile file) {
        try {
            resetCounters();
            if (file.isEmpty()) {
                return createErrorView(ErrorWadaManagement.E_0008_FILE_IS_EMPTY.getErrorMsg());
            }
            return processUploadedFile(file);
        } catch (Exception e) {
            return createErrorLoginView(e.getLocalizedMessage());
        }
    }

    private ModelAndView processUploadedFile(MultipartFile file) throws Exception {
        if (csvReaderService.readDataCheckLdapInsertIntoDB(file.getBytes())) {
            return createUploadSuccessView();
        }
        return createErrorView(ErrorWadaManagement.E_0007_LDAP_OR_DB_UNAVAILABLE.getErrorMsg());
    }

    private ModelAndView createUploadSuccessView() {
        return createBaseModelAndView(VIEW_UPLOAD_SUCCESS)
                .addObject("users_discarded", LDAPService.userNotExistsOnLdap)
                .addObject("users_uploaded", csvReaderService.getUsersUploaded());
    }

    private ModelAndView createErrorView(String errorMsg) {
        return createBaseModelAndView(VIEW_ERROR)
                .addObject("errorMsg", errorMsg);
    }

    private void resetCounters() {
        LDAPService.resetCounter();
        csvReaderService.resetCounter();
    }
}

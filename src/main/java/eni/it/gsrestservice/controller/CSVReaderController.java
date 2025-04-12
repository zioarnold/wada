package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.CSVReaderService;
import eni.it.gsrestservice.service.LDAPService;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.ora.QsAdminUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class CSVReaderController {
    private final CSVReaderService csvReaderService;
    private final QlikSenseService qlikSenseService;

    private final QsAdminUsersService qsAdminUsersService;

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage() {
        if (qsAdminUsersService.isAuthenticated(QsAdminUsers.username)) {
            if (qsAdminUsersService.checkSession(QsAdminUsers.username) == 1) {
                return new ModelAndView("massiveUploadPage")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseService.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else if (qsAdminUsersService.checkSession(QsAdminUsers.username) == -1) {
                return new ModelAndView("massiveUploadPage")
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
    }

    @RequestMapping(value = "/massiveUpload", method = RequestMethod.POST)
    public ModelAndView massiveUpload(@RequestParam("file") MultipartFile file) {
        try {
            resetCounters();
            if (!file.isEmpty()) {
                if (csvReaderService.readDataCheckLdapInsertIntoDB(file.getBytes())) {
                    return new ModelAndView("uploadSuccess")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseService.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role)
                            .addObject("users_discarded", LDAPService.userNotExistsOnLdap)
                            .addObject("users_uploaded", CSVReaderService.usersUploaded);
                } else {
                    return new ModelAndView("error")
                            .addObject("errorMsg", ErrorWadaManagement.E_0007_LDAP_OR_DB_UNAVAILABLE.getErrorMsg())
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseService.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                }
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0008_FILE_IS_EMPTY.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseService.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } catch (Exception e) {
            return new ModelAndView("errorLogin").addObject("errorMsg", e.getLocalizedMessage());
        }
    }

    private void resetCounters() {
        LDAPService.resetCounter();
        CSVReaderService.resetCounter();
    }
}

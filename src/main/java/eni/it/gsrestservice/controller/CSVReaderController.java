package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.model.*;
import eni.it.gsrestservice.service.CSVReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@RestController
public class CSVReaderController {
    @Autowired
    private CSVReaderService csvReaderService;
    @Autowired
    private Environment environment;
    private final DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
    private final DBConnectionOperationCentralized dbConnectionOperationCentralized = new DBConnectionOperationCentralized();
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final RolesListConfig rolesListConfig = new RolesListConfig();
    private int value;

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage() {
        try {
            initDB();
            initQlikConnector();
            if (DBConnectionOperationCentralized.isIsAuthenticated()) {
                if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("massiveUpload")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else if (dbConnectionOperationCentralized.checkSession(QsAdminUsers.username) == -1) {
                    return new ModelAndView("massiveUpload")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
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

    @GetMapping(value = "/massiveUpload")
    int uploadFileStatus() {
        int countRow = CSVReaderService.rowNumbers;
        value = (int) ((1.401 * 100) / countRow);
        return value++;
    }

    @RequestMapping(value = "/massiveUpload", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            initDB();
            initFile();
            resetCounters();
            initQlikConnector();
            if (!file.isEmpty()) {
                if (csvReaderService.readDataCheckLdapInsertIntoDB(file.getBytes())) {
                    return new ModelAndView("uploadSuccess")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role)
                            .addObject("users_discarded", LDAPConnector.userNotExistsOnLdap)
                            .addObject("users_uploaded", DBConnectionOperation.usersUploaded)
                            .addObject("users_processed", DBConnectionOperation.usersProcessed);
                } else {
                    return new ModelAndView("error")
                            .addObject("errorMsg", ErrorWadaManagement.E_0007_LDAP_OR_DB_UNAVAILABLE.getErrorMsg())
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                }
            } else {
                return new ModelAndView("error")
                        .addObject("errorMsg", ErrorWadaManagement.E_0008_FILE_IS_EMPTY.getErrorMsg())
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            }
        } catch (Exception e) {
            return new ModelAndView("errorLogin").addObject("errorMsg", e.getLocalizedMessage());
        }

    }

    private void resetCounters() {
        DBConnectionOperation.resetCounter();
        LDAPConnector.resetCounter();
        CSVReaderService.resetCounter();
        value = 0;
    }

    private void initFile() throws IOException {
        csvReaderService.initFile(
                environment.getProperty("log.discard"),
                environment.getProperty("log.user.role.discarded"));
        dbConnectionOperation.initFile(environment.getProperty("log.role.exist.for.user"));
        csvReaderService.setRolesList(rolesListConfig.initRolesList(environment.getProperty("roles.config.json.path")));
    }

    private void initDB() {
        dbConnectionOperation.initDB(
                Farm.dbHost,
                Farm.dbPort,
                Farm.dbSid,
                Farm.dbUser,
                Farm.dbPassword,
                environment.getProperty("db.tabuser"),
                environment.getProperty("db.tabattrib")
        );

        dbConnectionOperationCentralized.initDB(
                environment.getProperty("db.hostname.main"),
                environment.getProperty("db.port.main"),
                environment.getProperty("db.sid.main"),
                environment.getProperty("db.username.main"),
                environment.getProperty("db.password.main"),
                environment.getProperty("db.qs.admin.users"),
                environment.getProperty("db.qs.farms")
        );
    }

    private void initQlikConnector() throws Exception {
        qlikSenseConnector.initConnector(
                Farm.qsXrfKey,
                Farm.qsHost,
                Farm.qsPathClientJKS,
                Farm.qsPathRootJKS,
                Farm.qsKeyStorePwd,
                Farm.qsHeader,
                Farm.qsReloadTaskName);
        qlikSenseConnector.configureCertificate();
    }
}

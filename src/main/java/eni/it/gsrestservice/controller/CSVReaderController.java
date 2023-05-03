package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.Config;
import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.db.DBPostgresOperations;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.LDAPConnector;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.CSVReaderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Base64;

@RestController
public class CSVReaderController {
    private final CSVReaderService csvReaderService;
    private final DBPostgresOperations dbPostgresOperations = new DBPostgresOperations();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final RolesListConfig rolesListConfig = new RolesListConfig();

    public CSVReaderController(CSVReaderService csvReaderService) {
        this.csvReaderService = csvReaderService;
    }

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage() {
        initDB();
        initQlikConnector();
        if (DBOracleOperations.isIsAuthenticated()) {
            if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                return new ModelAndView("massiveUploadPage")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseConnector.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
                return new ModelAndView("massiveUploadPage")
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
    }

    @RequestMapping(value = "/massiveUpload", method = RequestMethod.POST)
    public ModelAndView massiveUpload(@RequestParam("file") MultipartFile file) {
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
                            .addObject("users_uploaded", DBPostgresOperations.usersUploaded)
                            .addObject("users_processed", DBPostgresOperations.usersProcessed);
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
        DBPostgresOperations.resetCounter();
        LDAPConnector.resetCounter();
        CSVReaderService.resetCounter();
    }

    private void initFile() throws IOException {
        csvReaderService.initFile(
                Config.logDiscard,
                Config.logUserRoleDiscarded);
        dbPostgresOperations.initFile(Config.logRoleExistForUser);
        csvReaderService.setRolesList(rolesListConfig.initRolesList(Config.rolesConfigJsonPath));
    }

    private void initDB() {
        initAllDBPostgresOracle(dbPostgresOperations, dbOracleOperations);
    }

    static void initAllDBPostgresOracle(DBPostgresOperations dbPostgresOperations, DBOracleOperations dbOracleOperations) {
        String decodedPassword = new String(Base64.getUrlDecoder().decode(Config.dbPasswordMain));
        dbPostgresOperations.initDB(
                Farm.dbHost,
                Farm.dbPort,
                Farm.dbSid,
                Farm.dbUser,
                Farm.dbPassword,
                Config.dbTabUsersTbl,
                Config.dbTabAttribTbl
        );
        dbOracleOperations.initDB(
                Config.dbHostnameMain,
                Config.dbPortMain,
                Config.dbSidMain,
                Config.dbUsernameMain,
                decodedPassword,
                Config.dbQsAdminUsersTbl,
                Config.dbQsFarmsTbl
        );
    }

    private void initQlikConnector() {
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

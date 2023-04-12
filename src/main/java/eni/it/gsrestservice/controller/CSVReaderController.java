package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.db.DBOracleOperations;
import eni.it.gsrestservice.db.DBPostgresOperations;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.LDAPConnector;
import eni.it.gsrestservice.model.QlikSenseConnector;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.CSVReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Base64;

@RestController
public class CSVReaderController {
    @Autowired
    private CSVReaderService csvReaderService;
    @Autowired
    private Environment environment;
    private final DBPostgresOperations dbPostgresOperations = new DBPostgresOperations();
    private final DBOracleOperations dbOracleOperations = new DBOracleOperations();
    private final QlikSenseConnector qlikSenseConnector = new QlikSenseConnector();
    private final RolesListConfig rolesListConfig = new RolesListConfig();
    private int value;
    private final String userRoleListJsonFileLocation = environment.getProperty("roles.config.json.path");

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage() {
        try {
            initDB();
            initQlikConnector();
            if (DBOracleOperations.isIsAuthenticated()) {
                if (dbOracleOperations.checkSession(QsAdminUsers.username) == 1) {
                    return new ModelAndView("massiveUpload")
                            .addObject("farm_name", Farm.description)
                            .addObject("farm_environment", Farm.environment)
                            .addObject("ping_qlik", qlikSenseConnector.ping())
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                } else if (dbOracleOperations.checkSession(QsAdminUsers.username) == -1) {
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
            if (initQlikConnector()) {
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
            } else {
                if (!file.isEmpty()) {
                    if (csvReaderService.readDataCheckLdapInsertIntoDB(file.getBytes())) {
                        return new ModelAndView("uploadSuccess")
                                .addObject("ping_qlik", 200)
                                .addObject("farm_name", "PIPPO")
                                .addObject("farm_environment", "DEV")
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role)
                                .addObject("users_discarded", LDAPConnector.userNotExistsOnLdap)
                                .addObject("users_uploaded", DBPostgresOperations.usersUploaded)
                                .addObject("users_processed", DBPostgresOperations.usersProcessed);
                    } else {
                        return new ModelAndView("error")
                                .addObject("errorMsg", ErrorWadaManagement.E_0007_LDAP_OR_DB_UNAVAILABLE.getErrorMsg())
                                .addObject("ping_qlik", 200)
                                .addObject("farm_name", "PIPPO")
                                .addObject("farm_environment", "DEV")
                                .addObject("user_logged_in", QsAdminUsers.username)
                                .addObject("user_role_logged_in", QsAdminUsers.role);
                    }
                } else {
                    return new ModelAndView("error")
                            .addObject("errorMsg", ErrorWadaManagement.E_0008_FILE_IS_EMPTY.getErrorMsg())
                            .addObject("ping_qlik", 200)
                            .addObject("farm_name", "PIPPO")
                            .addObject("farm_environment", "DEV")
                            .addObject("user_logged_in", QsAdminUsers.username)
                            .addObject("user_role_logged_in", QsAdminUsers.role);
                }
            }
        } catch (Exception e) {
            return new ModelAndView("errorLogin").addObject("errorMsg", e.getLocalizedMessage());
        }

    }

    private void resetCounters() {
        DBPostgresOperations.resetCounter();
        LDAPConnector.resetCounter();
        CSVReaderService.resetCounter();
        value = 0;
    }

    private void initFile() throws IOException {
        csvReaderService.initFile(
                environment.getProperty("log.discard"),
                environment.getProperty("log.user.role.discarded"));
        dbPostgresOperations.initFile(environment.getProperty("log.role.exist.for.user"));
        csvReaderService.setRolesList(rolesListConfig.initRolesList(userRoleListJsonFileLocation));
    }

    private void initDB() {
        initAllDBPostgresOracle(dbPostgresOperations, environment, dbOracleOperations);
    }

    static void initAllDBPostgresOracle(DBPostgresOperations dbPostgresOperations, Environment environment, DBOracleOperations dbOracleOperations) {
        dbPostgresOperations.initDB(
                Farm.dbHost,
                Farm.dbPort,
                Farm.dbSid,
                Farm.dbUser,
                Farm.dbPassword,
                environment.getProperty("db.tabuser"),
                environment.getProperty("db.tabattrib")
        );
        String decodedPassword = new String(Base64.getUrlDecoder().decode(environment.getProperty("db.password.main")));
        dbOracleOperations.initDB(
                environment.getProperty("db.hostname.main"),
                environment.getProperty("db.port.main"),
                environment.getProperty("db.sid.main"),
                environment.getProperty("db.username.main"),
                decodedPassword,
                environment.getProperty("db.qs.admin.users"),
                environment.getProperty("db.qs.farms")
        );
    }

    private boolean initQlikConnector() {
        qlikSenseConnector.initConnector(
                Farm.qsXrfKey,
                Farm.qsHost,
                Farm.qsPathClientJKS,
                Farm.qsPathRootJKS,
                Farm.qsKeyStorePwd,
                Farm.qsHeader,
                Farm.qsReloadTaskName);
        return qlikSenseConnector.configureCertificate();
    }
}

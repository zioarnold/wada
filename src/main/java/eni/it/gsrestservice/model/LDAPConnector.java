package eni.it.gsrestservice.model;

import eni.it.gsrestservice.config.LoggingMisc;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings({"DuplicatedCode", "ConstantConditions"})
@Configuration
@PropertySource("classpath:application.properties")
public class LDAPConnector implements EnvironmentAware {
    private static InitialDirContext initialDirContext;
    private LoggingMisc loggingMisc;
    private Properties properties;
    private SearchControls searchControl;
    private NamingEnumeration<SearchResult> answer;
    private Attribute displayName,
            eniRegistrationNumber,
            name,
            mail,
            givenName,
            sn,
            badPwdCount,
            pwdLastSet,
            userAccountDisabled,
            userDontExpirePassword,
            memberOf,
            ou;
    private LDAPUser ldapUser;
    private final List<LDAPUser> userExistsOnLdap = new ArrayList<>();
    private static Environment environment;
    private FileOutputStream fileOutputStream;
    public static int userNotExistsOnLdap;

    public static void resetCounter() {
        userNotExistsOnLdap = 0;
    }

    public LDAPConnector() {
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer ldapConnectorConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public List<LDAPUser> searchOnLDAP(String filter) throws IOException {
        loggingMisc = new LoggingMisc();
        properties = new Properties();
        ldapUser = new LDAPUser();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, environment.getProperty("vds.ldapURL"));
        properties.put(Context.SECURITY_PRINCIPAL, environment.getProperty("vds.userName"));
        properties.put(Context.SECURITY_CREDENTIALS, environment.getProperty("vds.password"));
        File fileUsersNotExists = new File(environment.getProperty("log.discard"));
        if (!fileUsersNotExists.exists()) {
            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                    " - Creating file: " + environment.getProperty("log.discard"));
            if (fileUsersNotExists.createNewFile()) {
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Creating file: " + environment.getProperty("log.discard") + " successful");
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Opening file: " + environment.getProperty("log.discard"));
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Opening file: " + environment.getProperty("log.discard") + " successful");
            } else {
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            }
        } else {
            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                    " - Opening file: " + environment.getProperty("log.discard"));
            fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                    " - Opening file: " + environment.getProperty("log.discard") + " successful");
        }
        try {
            initialDirContext = new InitialDirContext(properties);
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search(environment.getProperty("vds.baseDN"), "(ENIMatricolaNotes=" + filter + ")", searchControl);
            loggingMisc.printConsole(1, "Connection to LDAP");
            if (answer.hasMore()) {
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - Connection to LDAP Successful");
                do {
                    SearchResult result = answer.next();
                    loggingMisc.printConsole(1, "---------------------------------------------------------------------------");
                    loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - distinguishedName: " + result.getNameInNamespace());
                    String userDN = result.getNameInNamespace();
                    loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - getName: " + userDN);
                    displayName = result.getAttributes().get("displayName");
                    if (displayName != null) {
                        for (int idx = 0; idx < displayName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - displayName: " + displayName.get(idx).toString());
                        }
                    }
                    eniRegistrationNumber = result.getAttributes().get("ENIMatricolaNotes");
                    if (eniRegistrationNumber != null) {
                        for (int idx = 0; idx < eniRegistrationNumber.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - ENIMatricolaNotes: " + eniRegistrationNumber.get(idx).toString());
                        }
                    }
                    name = result.getAttributes().get("name");
                    if (name != null) {
                        for (int idx = 0; idx < name.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - name: " + name.get(idx).toString());
                        }
                    }
                    mail = result.getAttributes().get("mail");
                    if (mail != null) {
                        for (int idx = 0; idx < mail.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - mail: " + mail.get(idx).toString());
                        }
                    }
                    givenName = result.getAttributes().get("givenName");
                    if (givenName != null) {
                        for (int idx = 0; idx < givenName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - givenName: " + givenName.get(idx).toString());
                        }
                    }
                    sn = result.getAttributes().get("sn");
                    if (sn != null) {
                        for (int idx = 0; idx < sn.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - sn: " + sn.get(idx).toString());
                        }
                    }
                    badPwdCount = result.getAttributes().get("badPwdCount");
                    if (badPwdCount != null) {
                        for (int idx = 0; idx < badPwdCount.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - badPwdCount: " + badPwdCount.get(idx).toString());
                        }
                    }
                    pwdLastSet = result.getAttributes().get("pwdLastSet");
                    if (pwdLastSet != null) {
                        for (int idx = 0; idx < pwdLastSet.size(); idx++) {
                            String lastSetPwd = pwdLastSet.get(idx).toString();
                            String LastSetPwd = loggingMisc.timeStampToDate(lastSetPwd);
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - pwdLastSet: " + LastSetPwd);
                        }
                    }
                    userAccountDisabled = result.getAttributes().get("msDS-UserAccountDisabled");
                    if (userAccountDisabled != null) {
                        for (int idx = 0; idx < userAccountDisabled.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - UserAccountDisabled: " + userAccountDisabled.get(idx).toString());
                        }
                    }

                    userDontExpirePassword = result.getAttributes().get("msDS-UserDontExpirePassword");
                    if (userDontExpirePassword != null) {
                        for (int idx = 0; idx < userDontExpirePassword.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - UserDontExpirePassword: " + userDontExpirePassword.get(idx).toString());
                        }
                    }
                    memberOf = result.getAttributes().get("memberOf");
                    if (memberOf != null) {
                        for (int idx = 0; idx < memberOf.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - memberOf: " + memberOf.get(idx).toString());
                        }
                    }
                    ou = result.getAttributes().get("ou");
                    if (ou != null) {
                        for (int idx = 0; idx < ou.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - ou: " + ou.get(idx).toString());
                        }
                    }
                    if (displayName == null) {
                        ldapUser.setDisplayName("N/A");
                    } else {
                        ldapUser.setDisplayName(displayName.get().toString());
                    }
                    if (eniRegistrationNumber == null) {
                        ldapUser.setENIMatricolaNotes("N/A");
                    } else {
                        ldapUser.setENIMatricolaNotes(eniRegistrationNumber.get().toString());
                    }
                    if (name == null) {
                        ldapUser.setName("N/A");
                    } else {
                        ldapUser.setName(name.get().toString());
                    }
                    if (mail == null) {
                        ldapUser.setMail("N/A");
                    } else {
                        ldapUser.setMail(mail.get().toString());
                    }
                    if (givenName == null) {
                        ldapUser.setGivenName("N/A");
                    } else {
                        ldapUser.setGivenName(givenName.get().toString());
                    }
                    if (sn == null) {
                        ldapUser.setSn("N/A");
                    } else {
                        ldapUser.setSn(sn.get().toString());
                    }
                    if (badPwdCount == null) {
                        ldapUser.setBadPwdCount("N/A");
                    } else {
                        ldapUser.setBadPwdCount(badPwdCount.get().toString());
                    }
                    if (pwdLastSet == null) {
                        ldapUser.setPwdLastSet("N/A");
                    } else {
                        ldapUser.setPwdLastSet(pwdLastSet.get().toString());
                    }
                    if (userAccountDisabled == null) {
                        ldapUser.setUserAccountDisabled("N/A");
                    } else {
                        ldapUser.setUserAccountDisabled(userAccountDisabled.get().toString());
                    }
                    if (userDontExpirePassword == null) {
                        ldapUser.setUserDontExpirePassword("N/A");
                    } else {
                        ldapUser.setUserDontExpirePassword(userDontExpirePassword.get().toString());
                    }
                    if (memberOf == null) {
                        ldapUser.setMemberOf("N/A");
                    } else {
                        ldapUser.setMemberOf(memberOf.get().toString());
                    }
                    if (ou == null) {
                        ldapUser.setOu("N/A");
                    } else {
                        ldapUser.setOu(ou.get().toString());
                    }
                    userExistsOnLdap.add(ldapUser);
                } while (answer.hasMore());
            } else {
                String userNotExist = "Utenza non esiste sul VDS: " + filter + "\n";
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Writing to file: " + userNotExist);
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
                fileOutputStream.write(userNotExist.getBytes());
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Writing to file: " + userNotExist + " successful");
            }
            initialDirContext.close();
            answer.close();
            fileOutputStream.close();
        } catch (NamingException e) {
            loggingMisc.printConsole(2, LDAPConnector.class.getSimpleName() + " - Unable to connect to LDAP, check your properties: "
                    + e.getExplanation() + " " + e.getLocalizedMessage());
        }
        return userExistsOnLdap;
    }

    /**
     * @param userID criterio di ricerca x popolare il DB
     */
    public void searchOnLDAPInsertToDB(String userID, String userRole, String userGroup) throws IOException {
        loggingMisc = new LoggingMisc();
        properties = new Properties();
        DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
        dbConnectionOperation.connectDB();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, environment.getProperty("vds.ldapURL"));
        properties.put(Context.SECURITY_PRINCIPAL, environment.getProperty("vds.userName"));
        properties.put(Context.SECURITY_CREDENTIALS, environment.getProperty("vds.password"));
        ldapUser = new LDAPUser();
        File fileUsersNotExists = new File(environment.getProperty("log.discard"));
        loggingMisc.printConsole(1, "");
        if (!fileUsersNotExists.exists()) {
            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                    " - Creating file: " + environment.getProperty("log.discard"));
            if (fileUsersNotExists.createNewFile()) {
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Creating file: " + environment.getProperty("log.discard") + " successful");
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Opening file: " + environment.getProperty("log.discard"));
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Opening file: " + environment.getProperty("log.discard") + " successful");
            } else {
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            }
        } else {
            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                    " - Opening file: " + environment.getProperty("log.discard"));
            fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                    " - Opening file: " + environment.getProperty("log.discard") + " successful");
        }
        try {
            initialDirContext = new InitialDirContext(properties);
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search(environment.getProperty("vds.baseDN"), "(ENIMatricolaNotes=" + userID + ")", searchControl);
            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - Connection to LDAP");
            if (answer.hasMore()) {
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - Connection to LDAP Successful");
                do {
                    SearchResult result = answer.next();
                    loggingMisc.printConsole(1, "---------------------------------------------------------------------------");
                    loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - distinguishedName: " + result.getNameInNamespace());
                    String userDN = result.getNameInNamespace();
                    loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - getName: " + userDN);
                    displayName = result.getAttributes().get("displayName");
                    if (displayName != null) {
                        for (int idx = 0; idx < displayName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - displayName: " + displayName.get(idx).toString());
                        }
                    }
                    eniRegistrationNumber = result.getAttributes().get("ENIMatricolaNotes");
                    if (eniRegistrationNumber != null) {
                        for (int idx = 0; idx < eniRegistrationNumber.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - ENIMatricolaNotes: " + eniRegistrationNumber.get(idx).toString());
                        }
                    }
                    name = result.getAttributes().get("name");
                    if (name != null) {
                        for (int idx = 0; idx < name.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - name: " + name.get(idx).toString());
                        }
                    }
                    mail = result.getAttributes().get("mail");
                    if (mail != null) {
                        for (int idx = 0; idx < mail.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - mail: " + mail.get(idx).toString());
                        }
                    }
                    givenName = result.getAttributes().get("givenName");
                    if (givenName != null) {
                        for (int idx = 0; idx < givenName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - givenName: " + givenName.get(idx).toString());
                        }
                    }
                    sn = result.getAttributes().get("sn");
                    if (sn != null) {
                        for (int idx = 0; idx < sn.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - sn: " + sn.get(idx).toString());
                        }
                    }
                    badPwdCount = result.getAttributes().get("badPwdCount");
                    if (badPwdCount != null) {
                        for (int idx = 0; idx < badPwdCount.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - badPwdCount: " + badPwdCount.get(idx).toString());
                        }
                    }
                    pwdLastSet = result.getAttributes().get("pwdLastSet");
                    if (pwdLastSet != null) {
                        for (int idx = 0; idx < pwdLastSet.size(); idx++) {
                            String lastSetPwd = pwdLastSet.get(idx).toString();
                            String LastSetPwd = loggingMisc.timeStampToDate(lastSetPwd);
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - pwdLastSet: " + LastSetPwd);
                        }
                    }
                    userAccountDisabled = result.getAttributes().get("msDS-UserAccountDisabled");
                    if (userAccountDisabled != null) {
                        for (int idx = 0; idx < userAccountDisabled.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - UserAccountDisabled: " + userAccountDisabled.get(idx).toString());
                        }
                    }

                    userDontExpirePassword = result.getAttributes().get("msDS-UserDontExpirePassword");
                    if (userDontExpirePassword != null) {
                        for (int idx = 0; idx < userDontExpirePassword.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - UserDontExpirePassword: " + userDontExpirePassword.get(idx).toString());
                        }
                    }
                    memberOf = result.getAttributes().get("memberOf");
                    if (memberOf != null) {
                        for (int idx = 0; idx < memberOf.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - memberOf: " + memberOf.get(idx).toString());
                        }
                    }
                    ou = result.getAttributes().get("ou");
                    if (ou != null) {
                        for (int idx = 0; idx < ou.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() + " - ou: " + ou.get(idx).toString());
                        }
                    }
                    if (displayName != null) {
                        if (displayName.get().toString().contains("'")) {
                            String displayNameReplaced = displayName.get().toString().replace("'", "''");
                            dbConnectionOperation.insertToQSUsers(eniRegistrationNumber.get().toString(), displayNameReplaced, "Y");
                        } else {
                            dbConnectionOperation.insertToQSUsers(eniRegistrationNumber.get().toString(), displayName.get().toString(), "Y");
                        }
                    } else {
                        dbConnectionOperation.insertToQSUsers(eniRegistrationNumber.get().toString(), eniRegistrationNumber.get().toString(), "Y");
                    }
                    if (mail == null) {
                        dbConnectionOperation.insertQUserAttributeEmail(eniRegistrationNumber.get().toString(), "email", "N/A");
                    } else {
                        if (mail.get().toString().contains("'")) {
                            String mailReplaced = mail.get().toString().replace("'", "''");
                            dbConnectionOperation.insertQUserAttributeEmail(eniRegistrationNumber.get().toString(), "email", mailReplaced);
                        } else {
                            dbConnectionOperation.insertQUserAttributeEmail(eniRegistrationNumber.get().toString(), "email", mail.get().toString());
                        }
                    }
                    if (ou == null) {
                        dbConnectionOperation.insertQUserAttributeOU(eniRegistrationNumber.get().toString(), "organizzazione", "N/A");
                    } else {
                        dbConnectionOperation.insertQUserAttributeOU(eniRegistrationNumber.get().toString(), "organizzazione", ou.get().toString());
                    }
                    dbConnectionOperation.insertQUserAttribute(eniRegistrationNumber.get().toString(), "gruppo", userGroup);
                    dbConnectionOperation.insertQUserAttribute(eniRegistrationNumber.get().toString(), "ruolo", userRole);
                    if (displayName == null) {
                        ldapUser.setDisplayName("N/A");
                    } else {
                        ldapUser.setDisplayName(displayName.get().toString());
                    }
                    if (eniRegistrationNumber == null) {
                        ldapUser.setENIMatricolaNotes("N/A");
                    } else {
                        ldapUser.setENIMatricolaNotes(eniRegistrationNumber.get().toString());
                    }
                    if (name == null) {
                        ldapUser.setName("N/A");
                    } else {
                        ldapUser.setName(name.get().toString());
                    }
                    if (mail == null) {
                        ldapUser.setMail("N/A");
                    } else {
                        ldapUser.setMail(mail.get().toString());
                    }
                    if (givenName == null) {
                        ldapUser.setGivenName("N/A");
                    } else {
                        ldapUser.setGivenName(givenName.get().toString());
                    }
                    if (sn == null) {
                        ldapUser.setSn("N/A");
                    } else {
                        ldapUser.setSn(sn.get().toString());
                    }
                    if (badPwdCount == null) {
                        ldapUser.setBadPwdCount("N/A");
                    } else {
                        ldapUser.setBadPwdCount(badPwdCount.get().toString());
                    }
                    if (pwdLastSet == null) {
                        ldapUser.setPwdLastSet("N/A");
                    } else {
                        ldapUser.setPwdLastSet(pwdLastSet.get().toString());
                    }
                    if (userAccountDisabled == null) {
                        ldapUser.setUserAccountDisabled("N/A");
                    } else {
                        ldapUser.setUserAccountDisabled(userAccountDisabled.get().toString());
                    }
                    if (userDontExpirePassword == null) {
                        ldapUser.setUserDontExpirePassword("N/A");
                    } else {
                        ldapUser.setUserDontExpirePassword(userDontExpirePassword.get().toString());
                    }
                    if (memberOf == null) {
                        ldapUser.setMemberOf("N/A");
                    } else {
                        ldapUser.setMemberOf(memberOf.get().toString());
                    }
                    if (ou == null) {
                        ldapUser.setOu("N/A");
                    } else {
                        ldapUser.setOu(ou.get().toString());
                    }
                } while (answer.hasMore());
            } else {
                loggingMisc.printConsole(2, LDAPConnector.class.getSimpleName() + " - User not found by filter: " + userID);
                loggingMisc = new LoggingMisc();
                String userNotExist = "Utenza non esiste sul VDS: " + userID + "\n";
                userNotExistsOnLdap++;
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Writing to file: " + userNotExist);
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
                fileOutputStream.write(userNotExist.getBytes());
                loggingMisc.printConsole(1, LDAPConnector.class.getSimpleName() +
                        " - Writing to file: " + userNotExist + " successful");
            }
            initialDirContext.close();
            answer.close();
            fileOutputStream.close();
            dbConnectionOperation.disconnectDB();
        } catch (NamingException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        LDAPConnector.environment = environment;
    }
}

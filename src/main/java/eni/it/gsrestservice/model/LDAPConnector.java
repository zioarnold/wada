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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("DuplicatedCode")
@Configuration
@PropertySource("classpath:application.properties")
public class LDAPConnector implements EnvironmentAware {
    private static InitialDirContext initialDirContext;
    private LoggingMisc loggingMisc;
    private Properties properties;
    private SearchControls searchControl;
    private NamingEnumeration<SearchResult> answer;
    private Attribute displayName,
            eniMatricolaNotes,
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
    private List<LDAPUser> userExistsOnLdap;
    private List<LDAPUser> userNotExistsOnLdap;

    private static Environment environment;

    public LDAPConnector() {
        userExistsOnLdap = new ArrayList<>();
        userNotExistsOnLdap = new ArrayList<>();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer ldapConnectorConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public List<LDAPUser> searchOnLDAP(String filter) {
        loggingMisc = new LoggingMisc();
        properties = new Properties();
        ldapUser = new LDAPUser();
        userExistsOnLdap.clear();
        userNotExistsOnLdap.clear();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, environment.getProperty("vds.ldapURL"));
        properties.put(Context.SECURITY_PRINCIPAL, environment.getProperty("vds.userName"));
        properties.put(Context.SECURITY_CREDENTIALS, environment.getProperty("vds.password"));
        try {
            initialDirContext = new InitialDirContext(properties);
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search(environment.getProperty("vds.baseDN"), "(ENIMatricolaNotes=" + filter + ")", searchControl);
            loggingMisc.printConsole(1, "Connection to LDAP");
            if (answer.hasMore()) {
                loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "Connection to LDAP Successful");
                do {
                    SearchResult result = answer.next();
                    loggingMisc.printConsole(1, "---------------------------------------------------------------------------");
                    loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "distinguishedName: " + result.getNameInNamespace());
                    String userDN = result.getNameInNamespace();
                    loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "getName: " + userDN);
                    displayName = result.getAttributes().get("displayName");
                    if (displayName != null) {
                        for (int idx = 0; idx < displayName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "displayName: " + displayName.get(idx).toString());
                        }
                    }
                    eniMatricolaNotes = result.getAttributes().get("ENIMatricolaNotes");
                    if (eniMatricolaNotes != null) {
                        for (int idx = 0; idx < eniMatricolaNotes.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "ENIMatricolaNotes: " + eniMatricolaNotes.get(idx).toString());
                        }
                    }
                    name = result.getAttributes().get("name");
                    if (name != null) {
                        for (int idx = 0; idx < name.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "name: " + name.get(idx).toString());
                        }
                    }
                    mail = result.getAttributes().get("mail");
                    if (mail != null) {
                        for (int idx = 0; idx < mail.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "mail: " + mail.get(idx).toString());
                        }
                    }
                    givenName = result.getAttributes().get("givenName");
                    if (givenName != null) {
                        for (int idx = 0; idx < givenName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "givenName: " + givenName.get(idx).toString());
                        }
                    }
                    sn = result.getAttributes().get("sn");
                    if (sn != null) {
                        for (int idx = 0; idx < sn.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "sn: " + sn.get(idx).toString());
                        }
                    }
                    badPwdCount = result.getAttributes().get("badPwdCount");
                    if (badPwdCount != null) {
                        for (int idx = 0; idx < badPwdCount.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "badPwdCount: " + badPwdCount.get(idx).toString());
                        }
                    }
                    pwdLastSet = result.getAttributes().get("pwdLastSet");
                    if (pwdLastSet != null) {
                        for (int idx = 0; idx < pwdLastSet.size(); idx++) {
                            String lastSetPwd = pwdLastSet.get(idx).toString();
                            String LastSetPwd = loggingMisc.timeStampToDate(lastSetPwd);
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "pwdLastSet: " + LastSetPwd);
                        }
                    }
                    userAccountDisabled = result.getAttributes().get("msDS-UserAccountDisabled");
                    if (userAccountDisabled != null) {
                        for (int idx = 0; idx < userAccountDisabled.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "UserAccountDisabled: " + userAccountDisabled.get(idx).toString());
                        }
                    }

                    userDontExpirePassword = result.getAttributes().get("msDS-UserDontExpirePassword");
                    if (userDontExpirePassword != null) {
                        for (int idx = 0; idx < userDontExpirePassword.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "UserDontExpirePassword: " + userDontExpirePassword.get(idx).toString());
                        }
                    }
                    memberOf = result.getAttributes().get("memberOf");
                    if (memberOf != null) {
                        for (int idx = 0; idx < memberOf.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "memberOf: " + memberOf.get(idx).toString());
                        }
                    }
                    ou = result.getAttributes().get("ou");
                    if (ou != null) {
                        for (int idx = 0; idx < ou.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "ou: " + ou.get(idx).toString());
                        }
                    }
                    if (displayName == null) {
                        ldapUser.setDisplayName("N/A");
                    } else {
                        ldapUser.setDisplayName(displayName.get().toString());
                    }
                    if (eniMatricolaNotes == null) {
                        ldapUser.setENIMatricolaNotes("N/A");
                    } else {
                        ldapUser.setENIMatricolaNotes(eniMatricolaNotes.get().toString());
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
                    userExistsOnLdap.add(new LDAPUser(ldapUser));
                } while (answer.hasMore());
            } else {
                loggingMisc.printConsole(2, LDAPConnector.class.getName() + " " + "User not found by filter: " + filter);
                ldapUser.setENIMatricolaNotes(filter);
                userNotExistsOnLdap.add(new LDAPUser(ldapUser));
            }
            initialDirContext.close();
            answer.close();
        } catch (NamingException e) {
            loggingMisc.printConsole(2, LDAPConnector.class.getName() + " " + "Unable to connect to LDAP, check your properties: "
                    + e.getExplanation() + " " + e.getLocalizedMessage());
        }
        return userExistsOnLdap;
    }

    /**
     * @param userID criterio di ricerca x popolare il DB
     */
    public void searchOnLDAPInsertToDB(String userID, String userRole, String userGroup) {
        loggingMisc = new LoggingMisc();
        properties = new Properties();
        DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();
        dbConnectionOperation.connectDB();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, environment.getProperty("vds.ldapURL"));
        properties.put(Context.SECURITY_PRINCIPAL, environment.getProperty("vds.userName"));
        properties.put(Context.SECURITY_CREDENTIALS, environment.getProperty("vds.password"));
        ldapUser = new LDAPUser();
        try {
            initialDirContext = new InitialDirContext(properties);
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search(environment.getProperty("vds.baseDN"), "(ENIMatricolaNotes=" + userID + ")", searchControl);
            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "Connection to LDAP");
            if (answer.hasMore()) {
                loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "Connection to LDAP Successful");
                do {
                    SearchResult result = answer.next();
                    loggingMisc.printConsole(1, "---------------------------------------------------------------------------");
                    loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "distinguishedName: " + result.getNameInNamespace());
                    String userDN = result.getNameInNamespace();
                    loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "getName: " + userDN);
                    displayName = result.getAttributes().get("displayName");
                    if (displayName != null) {
                        for (int idx = 0; idx < displayName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "displayName: " + displayName.get(idx).toString());
                        }
                    }
                    eniMatricolaNotes = result.getAttributes().get("ENIMatricolaNotes");
                    if (eniMatricolaNotes != null) {
                        for (int idx = 0; idx < eniMatricolaNotes.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "ENIMatricolaNotes: " + eniMatricolaNotes.get(idx).toString());
                        }
                    }
                    name = result.getAttributes().get("name");
                    if (name != null) {
                        for (int idx = 0; idx < name.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "name: " + name.get(idx).toString());
                        }
                    }
                    mail = result.getAttributes().get("mail");
                    if (mail != null) {
                        for (int idx = 0; idx < mail.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "mail: " + mail.get(idx).toString());
                        }
                    }
                    givenName = result.getAttributes().get("givenName");
                    if (givenName != null) {
                        for (int idx = 0; idx < givenName.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "givenName: " + givenName.get(idx).toString());
                        }
                    }
                    sn = result.getAttributes().get("sn");
                    if (sn != null) {
                        for (int idx = 0; idx < sn.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "sn: " + sn.get(idx).toString());
                        }
                    }
                    badPwdCount = result.getAttributes().get("badPwdCount");
                    if (badPwdCount != null) {
                        for (int idx = 0; idx < badPwdCount.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "badPwdCount: " + badPwdCount.get(idx).toString());
                        }
                    }
                    pwdLastSet = result.getAttributes().get("pwdLastSet");
                    if (pwdLastSet != null) {
                        for (int idx = 0; idx < pwdLastSet.size(); idx++) {
                            String lastSetPwd = pwdLastSet.get(idx).toString();
                            String LastSetPwd = loggingMisc.timeStampToDate(lastSetPwd);
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "pwdLastSet: " + LastSetPwd);
                        }
                    }
                    userAccountDisabled = result.getAttributes().get("msDS-UserAccountDisabled");
                    if (userAccountDisabled != null) {
                        for (int idx = 0; idx < userAccountDisabled.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "UserAccountDisabled: " + userAccountDisabled.get(idx).toString());
                        }
                    }

                    userDontExpirePassword = result.getAttributes().get("msDS-UserDontExpirePassword");
                    if (userDontExpirePassword != null) {
                        for (int idx = 0; idx < userDontExpirePassword.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "UserDontExpirePassword: " + userDontExpirePassword.get(idx).toString());
                        }
                    }
                    memberOf = result.getAttributes().get("memberOf");
                    if (memberOf != null) {
                        for (int idx = 0; idx < memberOf.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "memberOf: " + memberOf.get(idx).toString());
                        }
                    }
                    ou = result.getAttributes().get("ou");
                    if (ou != null) {
                        for (int idx = 0; idx < ou.size(); idx++) {
                            loggingMisc.printConsole(1, LDAPConnector.class.getName() + " " + "ou: " + ou.get(idx).toString());
                        }
                    }
                    if (name == null && displayName == null && givenName != null) {
                        dbConnectionOperation.insertToQSUsers(eniMatricolaNotes.get().toString(), givenName.get().toString(), "Y");
                    } else if (name != null && displayName == null && givenName == null) {
                        dbConnectionOperation.insertToQSUsers(eniMatricolaNotes.get().toString(), name.get().toString(), "Y");
                    } else if (name == null && displayName != null && givenName == null) {
                        dbConnectionOperation.insertToQSUsers(eniMatricolaNotes.get().toString(), displayName.get().toString(), "Y");
                    } else {
                        dbConnectionOperation.insertToQSUsers(eniMatricolaNotes.get().toString(), eniMatricolaNotes.get().toString(), "Y");
                    }
                    if (mail == null) {
                        dbConnectionOperation.insertQUserAttributeEmail(eniMatricolaNotes.get().toString(), "email", "N/A");
                    } else {
                        dbConnectionOperation.insertQUserAttributeEmail(eniMatricolaNotes.get().toString(), "email", mail.get().toString());
                    }
                    if (ou == null) {
                        dbConnectionOperation.insertQUserAttributeOU(eniMatricolaNotes.get().toString(), "organizzazione", "N/A");
                    } else {
                        dbConnectionOperation.insertQUserAttributeOU(eniMatricolaNotes.get().toString(), "organizzazione", ou.get().toString());
                    }
                    dbConnectionOperation.insertQUserAttribute(eniMatricolaNotes.get().toString(), "gruppo", userGroup);
                    dbConnectionOperation.insertQUserAttribute(eniMatricolaNotes.get().toString(), "ruolo", userRole);
                    ldapUser.setENIMatricolaNotes(eniMatricolaNotes.get().toString());
                    userExistsOnLdap.add(new LDAPUser(ldapUser));
                } while (answer.hasMore());
            } else {
                loggingMisc.printConsole(2, LDAPConnector.class.getName() + " " + "User not found by filter: " + userID);
                ldapUser.setENIMatricolaNotes(userID);
                userNotExistsOnLdap.add(new LDAPUser(ldapUser));
            }
            initialDirContext.close();
            answer.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void clearUsersExistsOnLDAP() {
        userExistsOnLdap.clear();
    }

    public void clearUsersNotExistsOnLDAP() {
        userNotExistsOnLdap.clear();
    }

    public List<LDAPUser> getUserExistsOnLdap() {
        return userExistsOnLdap;
    }

    public List<LDAPUser> getUserNotExistsOnLdap() {
        return userNotExistsOnLdap;
    }

    @Override
    public void setEnvironment(Environment environment) {
        LDAPConnector.environment = environment;
    }
}

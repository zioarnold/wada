package eni.it.gsrestservice.model;

import eni.it.gsrestservice.config.LoggingMisc;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Properties;

public class LDAPConnector {
    private static String ldapURL;
    private static String ldapUserName;
    private static String ldapBaseDN;
    private static String ldapPassword;
    private static InitialDirContext initialDirContext;
    private LoggingMisc loggingMisc;
    private Properties properties;
    private SearchControls searchControl;
    private NamingEnumeration<SearchResult> answer;
    private SearchResult result;
    private DBConnectionOperation dbConnectionOperation;
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

    LDAPConnector() {

    }

    public LDAPConnector(Attribute displayName, Attribute eniMatricolaNotes,
                         Attribute name, Attribute mail, Attribute givenName,
                         Attribute sn, Attribute badPwdCount, Attribute pwdLastSet,
                         Attribute userAccountDisabled, Attribute userDontExpirePassword,
                         Attribute memberOf, Attribute ou) {
        this.displayName = displayName;
        this.eniMatricolaNotes = eniMatricolaNotes;
        this.name = name;
        this.mail = mail;
        this.givenName = givenName;
        this.sn = sn;
        this.badPwdCount = badPwdCount;
        this.pwdLastSet = pwdLastSet;
        this.userAccountDisabled = userAccountDisabled;
        this.userDontExpirePassword = userDontExpirePassword;
        this.memberOf = memberOf;
        this.ou = ou;
    }

    void searchOnLDAP(String filter) {
        loggingMisc = new LoggingMisc();
        properties = new Properties();
        dbConnectionOperation = new DBConnectionOperation();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, this.getLdapURL());
        properties.put(Context.SECURITY_PRINCIPAL, this.getLdapUserName());
        properties.put(Context.SECURITY_CREDENTIALS, this.getLdapPassword());
        try {
            initialDirContext = new InitialDirContext(properties);
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search(ldapBaseDN, "(ENIMatricolaNotes=" + filter + ")", searchControl);
            loggingMisc.printConsole(1, "Connection to LDAP");
            if (answer.hasMore()) {
                loggingMisc.printConsole(1, "Connection to LDAP Successful");
                do {
                    result = answer.next();
                    loggingMisc.printConsole(1, "---------------------------------------------------------------------------");
                    loggingMisc.printConsole(1, "distinguishedName: " + result.getNameInNamespace());
                    String userDN = result.getNameInNamespace();
                    loggingMisc.printConsole(1, "getName: " + userDN);
                    displayName = result.getAttributes().get("displayName");
                    if (displayName != null) {
                        for (int idx = 0; idx < displayName.size(); idx++) {
                            loggingMisc.printConsole(1, "displayName: " + displayName.get(idx).toString());
                        }
                    }
                    eniMatricolaNotes = result.getAttributes().get("ENIMatricolaNotes");
                    if (eniMatricolaNotes != null) {
                        for (int idx = 0; idx < eniMatricolaNotes.size(); idx++) {
                            loggingMisc.printConsole(1, "ENIMatricolaNotes: " + eniMatricolaNotes.get(idx).toString());
                        }
                    }
                    name = result.getAttributes().get("name");
                    if (name != null) {
                        for (int idx = 0; idx < name.size(); idx++) {
                            loggingMisc.printConsole(1, "name: " + name.get(idx).toString());
                        }
                    }
                    mail = result.getAttributes().get("mail");
                    if (mail != null) {
                        for (int idx = 0; idx < mail.size(); idx++) {
                            loggingMisc.printConsole(1, "mail: " + mail.get(idx).toString());
                        }
                    }
                    givenName = result.getAttributes().get("givenName");
                    if (givenName != null) {
                        for (int idx = 0; idx < givenName.size(); idx++) {
                            loggingMisc.printConsole(1, "givenName: " + givenName.get(idx).toString());
                        }
                    }
                    sn = result.getAttributes().get("sn");
                    if (sn != null) {
                        for (int idx = 0; idx < sn.size(); idx++) {
                            loggingMisc.printConsole(1, "sn: " + sn.get(idx).toString());
                        }
                    }
                    badPwdCount = result.getAttributes().get("badPwdCount");
                    if (badPwdCount != null) {
                        for (int idx = 0; idx < badPwdCount.size(); idx++) {
                            loggingMisc.printConsole(1, "badPwdCount: " + badPwdCount.get(idx).toString());
                        }
                    }
                    pwdLastSet = result.getAttributes().get("pwdLastSet");
                    if (pwdLastSet != null) {
                        for (int idx = 0; idx < pwdLastSet.size(); idx++) {
                            String lastSetPwd = pwdLastSet.get(idx).toString();
                            String LastSetPwd = loggingMisc.timeStampToDate(lastSetPwd);
                            loggingMisc.printConsole(1, "pwdLastSet: " + LastSetPwd);
                            //printConsole(1, "pwdLastSet: " + pwdLastSet.get(idx).toString());
                        }
                    }
                    userAccountDisabled = result.getAttributes().get("msDS-UserAccountDisabled");
                    if (userAccountDisabled != null) {
                        for (int idx = 0; idx < userAccountDisabled.size(); idx++) {
                            loggingMisc.printConsole(1, "UserAccountDisabled: " + userAccountDisabled.get(idx).toString());
                        }
                    }

                    userDontExpirePassword = result.getAttributes().get("msDS-UserDontExpirePassword");
                    if (userDontExpirePassword != null) {
                        for (int idx = 0; idx < userDontExpirePassword.size(); idx++) {
                            loggingMisc.printConsole(1, "UserDontExpirePassword: " + userDontExpirePassword.get(idx).toString());
                        }
                    }
                    memberOf = result.getAttributes().get("memberOf");
                    if (memberOf != null) {
                        for (int idx = 0; idx < memberOf.size(); idx++) {
                            loggingMisc.printConsole(1, "memberOf: " + memberOf.get(idx).toString());
                        }
                    }
                    ou = result.getAttributes().get("ou");
                    if (ou != null) {
                        for (int idx = 0; idx < ou.size(); idx++) {
                            loggingMisc.printConsole(1, "ou: " + ou.get(idx).toString());
                        }
                    }
                } while (answer.hasMore());
            } else {
                loggingMisc.printConsole(2, "User not found by filter: " + filter);
            }
            initialDirContext.close();
            answer.close();
        } catch (NamingException e) {
            loggingMisc.printConsole(2, "Unable to connect to LDAP, check your properties: "
                    + e.getExplanation() + " " + e.getLocalizedMessage());
        }
    }

    /**
     * @param filter criterio di ricerca x popolare il DB
     */
    void searchOnLDAPInsertToDB(String filter) {
        loggingMisc = new LoggingMisc();
        properties = new Properties();
        dbConnectionOperation = new DBConnectionOperation();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, this.getLdapURL());
        properties.put(Context.SECURITY_PRINCIPAL, this.getLdapUserName());
        properties.put(Context.SECURITY_CREDENTIALS, this.getLdapPassword());

        try {
            initialDirContext = new InitialDirContext(properties);
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search(ldapBaseDN, "(ENIMatricolaNotes=" + filter + ")", searchControl);
            loggingMisc.printConsole(1, "Connection to LDAP");
            if (answer.hasMore()) {
                loggingMisc.printConsole(1, "Connection to LDAP Successful");
                do {
                    result = answer.next();
                    loggingMisc.printConsole(1, "---------------------------------------------------------------------------");
                    loggingMisc.printConsole(1, "distinguishedName: " + result.getNameInNamespace());
                    String userDN = result.getNameInNamespace();
                    loggingMisc.printConsole(1, "getName: " + userDN);
                    displayName = result.getAttributes().get("displayName");
                    if (displayName != null) {
                        for (int idx = 0; idx < displayName.size(); idx++) {
                            loggingMisc.printConsole(1, "displayName: " + displayName.get(idx).toString());
                        }
                    }
                    eniMatricolaNotes = result.getAttributes().get("ENIMatricolaNotes");
                    if (eniMatricolaNotes != null) {
                        for (int idx = 0; idx < eniMatricolaNotes.size(); idx++) {
                            loggingMisc.printConsole(1, "ENIMatricolaNotes: " + eniMatricolaNotes.get(idx).toString());
                        }
                    }
                    name = result.getAttributes().get("name");
                    if (name != null) {
                        for (int idx = 0; idx < name.size(); idx++) {
                            loggingMisc.printConsole(1, "name: " + name.get(idx).toString());
                        }
                    }
                    mail = result.getAttributes().get("mail");
                    if (mail != null) {
                        for (int idx = 0; idx < mail.size(); idx++) {
                            loggingMisc.printConsole(1, "mail: " + mail.get(idx).toString());
                        }
                    }
                    givenName = result.getAttributes().get("givenName");
                    if (givenName != null) {
                        for (int idx = 0; idx < givenName.size(); idx++) {
                            loggingMisc.printConsole(1, "givenName: " + givenName.get(idx).toString());
                        }
                    }
                    sn = result.getAttributes().get("sn");
                    if (sn != null) {
                        for (int idx = 0; idx < sn.size(); idx++) {
                            loggingMisc.printConsole(1, "sn: " + sn.get(idx).toString());
                        }
                    }
                    badPwdCount = result.getAttributes().get("badPwdCount");
                    if (badPwdCount != null) {
                        for (int idx = 0; idx < badPwdCount.size(); idx++) {
                            loggingMisc.printConsole(1, "badPwdCount: " + badPwdCount.get(idx).toString());
                        }
                    }
                    pwdLastSet = result.getAttributes().get("pwdLastSet");
                    if (pwdLastSet != null) {
                        for (int idx = 0; idx < pwdLastSet.size(); idx++) {
                            String lastSetPwd = pwdLastSet.get(idx).toString();
                            String LastSetPwd = loggingMisc.timeStampToDate(lastSetPwd);
                            loggingMisc.printConsole(1, "pwdLastSet: " + LastSetPwd);
                            //printConsole(1, "pwdLastSet: " + pwdLastSet.get(idx).toString());
                        }
                    }
                    userAccountDisabled = result.getAttributes().get("msDS-UserAccountDisabled");
                    if (userAccountDisabled != null) {
                        for (int idx = 0; idx < userAccountDisabled.size(); idx++) {
                            loggingMisc.printConsole(1, "UserAccountDisabled: " + userAccountDisabled.get(idx).toString());
                        }
                    }

                    userDontExpirePassword = result.getAttributes().get("msDS-UserDontExpirePassword");
                    if (userDontExpirePassword != null) {
                        for (int idx = 0; idx < userDontExpirePassword.size(); idx++) {
                            loggingMisc.printConsole(1, "UserDontExpirePassword: " + userDontExpirePassword.get(idx).toString());
                        }
                    }
                    memberOf = result.getAttributes().get("memberOf");
                    if (memberOf != null) {
                        for (int idx = 0; idx < memberOf.size(); idx++) {
                            loggingMisc.printConsole(1, "memberOf: " + memberOf.get(idx).toString());
                        }
                    }
                    ou = result.getAttributes().get("ou");
                    if (ou != null) {
                        for (int idx = 0; idx < ou.size(); idx++) {
                            loggingMisc.printConsole(1, "ou: " + ou.get(idx).toString());
                        }
                    }
                    dbConnectionOperation.insertToFarmQSense(eniMatricolaNotes.get().toString(),
                            "1", "LALA", "NA NA ", "SVILUPPO");
                    if (displayName == null) {
                        dbConnectionOperation.insertQUser(eniMatricolaNotes.get().toString(), eniMatricolaNotes.get().toString());
                    } else {
                        dbConnectionOperation.insertQUser(eniMatricolaNotes.get().toString(), displayName.get().toString());
                    }

//                    dbConnectionOperation.insertUsers();
                } while (answer.hasMore());
            } else {
                loggingMisc.printConsole(2, "User not found by filter: " + filter);
            }
            initialDirContext.close();
            answer.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param ldapURL      hostname del LDAP
     * @param ldapUserName username x ldap
     * @param ldapPassword password x ldap
     * @param ldapBaseDN   base domain name x ldap
     * @method connectToLDAP esegue soltanto una connessione per capire se e' online o no.
     */
    void connectToLDAP(String ldapURL, String ldapUserName, String ldapPassword, String ldapBaseDN) {
        loggingMisc = new LoggingMisc();
        properties = new Properties();
        setLdapURL(ldapURL);
        setLdapUserName(ldapUserName);
        setLdapPassword(ldapPassword);
        setLdapBaseDN(ldapBaseDN);
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, getLdapURL());
        properties.put(Context.SECURITY_PRINCIPAL, getLdapUserName());
        properties.put(Context.SECURITY_CREDENTIALS, getLdapPassword());
//        Hashtable<String, String> authEnv = new Hashtable<>();
        //authEnv.put(Context.SECURITY_AUTHENTICATION, ldapAuth);
        try {
            initialDirContext = new InitialDirContext(properties);
            String filter = "(ENIMatricolaNotes=en30080)";
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search(ldapBaseDN, filter, searchControl);
            loggingMisc.printConsole(1, "Connection to LDAP");
            if (answer.hasMore()) {
                loggingMisc.printConsole(1, "Connection to LDAP Successful");
            } else {
                loggingMisc.printConsole(2, "Connection to LDAP failed");
            }
            initialDirContext.close();
            answer.close();
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private String getLdapURL() {
        return ldapURL;
    }

    private void setLdapURL(String ldapURL) {
        LDAPConnector.ldapURL = ldapURL;
    }

    private String getLdapUserName() {
        return ldapUserName;
    }

    private void setLdapUserName(String ldapUserName) {
        LDAPConnector.ldapUserName = ldapUserName;
    }

    private void setLdapBaseDN(String ldapBaseDN) {
        LDAPConnector.ldapBaseDN = ldapBaseDN;
    }

    private String getLdapPassword() {
        return ldapPassword;
    }

    private void setLdapPassword(String ldapPassword) {
        LDAPConnector.ldapPassword = ldapPassword;
    }
}

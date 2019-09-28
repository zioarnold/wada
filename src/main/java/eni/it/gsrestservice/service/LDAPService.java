package eni.it.gsrestservice.service;

import eni.it.gsrestservice.config.LoggingMisc;
import eni.it.gsrestservice.dao.LDAPRepository;
import eni.it.gsrestservice.model.LDAPUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
public class LDAPService {
    private static InitialDirContext initialDirContext;
    @Autowired
    private final LDAPRepository ldapRepository;
    private LDAPUsers ldapUsers = new LDAPUsers();
    private LoggingMisc loggingMisc;
    private Properties properties;
    private SearchControls searchControl;
    private NamingEnumeration<SearchResult> answer;
    private SearchResult result;

    @Autowired
    private Environment environment;
    @Autowired
    private ContextSource contextSource;
    @Autowired
    private LdapTemplate ldapTemplate;

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

    public LDAPService(LDAPRepository ldapRepository) {
        this.ldapRepository = ldapRepository;
    }

    public List<LDAPUsers> searchUserOnLdap(String filter) {
        properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, "ldap://vds-test.services.eni.intranet:18389/");
        properties.put(Context.SECURITY_PRINCIPAL, "cn=adminLDAP-QLIK,ou=guest,o=ea-tree");
        properties.put(Context.SECURITY_CREDENTIALS, "B3%jUNwb");
        try {
            initialDirContext = new InitialDirContext(properties);
            searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            answer = initialDirContext.search("dc=pri", "(ENIMatricolaNotes=" + filter + ")", searchControl);
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
                            ldapUsers.setDisplayName(displayName.get(idx).toString());
                        }
                    }
                    eniMatricolaNotes = result.getAttributes().get("ENIMatricolaNotes");
                    if (eniMatricolaNotes != null) {
                        for (int idx = 0; idx < eniMatricolaNotes.size(); idx++) {
                            loggingMisc.printConsole(1, "ENIMatricolaNotes: " + eniMatricolaNotes.get(idx).toString());
                            ldapUsers.setUserID(eniMatricolaNotes.get(idx).toString());
                        }
                    }
                    name = result.getAttributes().get("name");
                    if (name != null) {
                        for (int idx = 0; idx < name.size(); idx++) {
                            loggingMisc.printConsole(1, "name: " + name.get(idx).toString());
                            ldapUsers.setName(name.get(idx).toString());
                        }
                    }
                    mail = result.getAttributes().get("mail");
                    if (mail != null) {
                        for (int idx = 0; idx < mail.size(); idx++) {
                            loggingMisc.printConsole(1, "mail: " + mail.get(idx).toString());
                            ldapUsers.setMail(mail.get(idx).toString());
                        }
                    }
                    givenName = result.getAttributes().get("givenName");
                    if (givenName != null) {
                        for (int idx = 0; idx < givenName.size(); idx++) {
                            loggingMisc.printConsole(1, "givenName: " + givenName.get(idx).toString());
                            ldapUsers.setGivenName(givenName.get(idx).toString());
                        }
                    }
                    sn = result.getAttributes().get("sn");
                    if (sn != null) {
                        for (int idx = 0; idx < sn.size(); idx++) {
                            loggingMisc.printConsole(1, "sn: " + sn.get(idx).toString());
                            ldapUsers.setSn(sn.get(idx).toString());
                        }
                    }
                    badPwdCount = result.getAttributes().get("badPwdCount");
                    if (badPwdCount != null) {
                        for (int idx = 0; idx < badPwdCount.size(); idx++) {
                            loggingMisc.printConsole(1, "badPwdCount: " + badPwdCount.get(idx).toString());
                            ldapUsers.setBadPwdCount(badPwdCount.get(idx).toString());
                        }
                    }
                    pwdLastSet = result.getAttributes().get("pwdLastSet");
                    if (pwdLastSet != null) {
                        for (int idx = 0; idx < pwdLastSet.size(); idx++) {
                            String lastSetPwd = pwdLastSet.get(idx).toString();
                            String LastSetPwd = loggingMisc.timeStampToDate(lastSetPwd);
                            loggingMisc.printConsole(1, "pwdLastSet: " + LastSetPwd);
                            ldapUsers.setPwdLastSet(LastSetPwd);
                            //printConsole(1, "pwdLastSet: " + pwdLastSet.get(idx).toString());
                        }
                    }
                    userAccountDisabled = result.getAttributes().get("msDS-UserAccountDisabled");
                    if (userAccountDisabled != null) {
                        for (int idx = 0; idx < userAccountDisabled.size(); idx++) {
                            loggingMisc.printConsole(1, "UserAccountDisabled: " + userAccountDisabled.get(idx).toString());
                            ldapUsers.setUserAccountDisabled(userAccountDisabled.get(idx).toString());
                        }
                    }

                    userDontExpirePassword = result.getAttributes().get("msDS-UserDontExpirePassword");
                    if (userDontExpirePassword != null) {
                        for (int idx = 0; idx < userDontExpirePassword.size(); idx++) {
                            loggingMisc.printConsole(1, "UserDontExpirePassword: " + userDontExpirePassword.get(idx).toString());
                            ldapUsers.setUserDontExpirePassword(userDontExpirePassword.get(idx).toString());
                        }
                    }
                    memberOf = result.getAttributes().get("memberOf");
                    if (memberOf != null) {
                        for (int idx = 0; idx < memberOf.size(); idx++) {
                            loggingMisc.printConsole(1, "memberOf: " + memberOf.get(idx).toString());
                            ldapUsers.setMemberOf(memberOf.get(idx).toString());
                        }
                    }
                    ou = result.getAttributes().get("ou");
                    if (ou != null) {
                        for (int idx = 0; idx < ou.size(); idx++) {
                            loggingMisc.printConsole(1, "ou: " + ou.get(idx).toString());
                            ldapUsers.setOu(ou.get(idx).toString());
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
        return Collections.singletonList(ldapUsers);
    }

    public void authenticate(String username, String password) {
        contextSource.getContext("cn=" + username + ",cn=users," + environment.getRequiredProperty("ldap.partitionSuffix"), password);
    }

    //EniDesignatedNumber - ENIMatricolaNotes
    public List<String> findByEniDesignatedNumber(String cn) {
//        return ldapRepository.findOne(query().where("ENIMatricolaNotes").is(cn), LDAPUsers.class);
//        return ldapRepository.findOne(query().where("ENIMatricolaNotes").is(cn), LDAPUsers.class);
        return ldapTemplate.search(
                "ou=users",
                "cn=" + cn,
                (AttributesMapper<String>) attrs -> (String) attrs.get("cn").get());
    }

    public List<LDAPUsers> findAll() {
        List<LDAPUsers> users = new ArrayList<>();
        for (LDAPUsers u : ldapRepository.findAll()) {
            users.add(u);
        }
        return users;
    }

    public LDAPUsers create(LDAPUsers person) {
        ldapTemplate.create(person);
        return person;
    }

    public void update(LDAPUsers person) {
        ldapTemplate.update(person);
    }
}

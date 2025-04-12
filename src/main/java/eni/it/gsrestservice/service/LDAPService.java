package eni.it.gsrestservice.service;


import eni.it.gsrestservice.entities.postgres.QsUser;
import eni.it.gsrestservice.entities.postgres.QsUsersAttrib;
import eni.it.gsrestservice.model.LDAPUser;
import eni.it.gsrestservice.service.post.QsUsersAttributesService;
import eni.it.gsrestservice.service.post.QsUsersService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

/**
 * @author Zio Arnold aka Arni
 * @created 12/04/2025 - 15:23 </br>
 */
@Service
@Configuration
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class LDAPService {
    @Value("${vds.context.factory}")
    private static String contextFactory;
    @Value("${vds.baseDN}")
    private static String baseDN;
    @Value("${vds.ldapURL}")
    private static String ldapURL;
    @Value("${vds.userName}")
    private static String userName;
    @Value("${vds.password}")
    private static String password;
    @Value("${log.discard}")
    private static String logDiscard;
    @Value("${db.tabattrib}")
    private static String tabAttribute;

    public static int userNotExistsOnLdap;

    private final QsUsersService qsUsersService;
    private final QsUsersAttributesService qsUsersAttributesService;

    public static void resetCounter() {
        userNotExistsOnLdap = 0;
    }

    public List<LDAPUser> searchOnLDAP(String filter) throws IOException {
        List<LDAPUser> userExistsOnLdap = new ArrayList<>();
        String decodedPassword = new String(Base64.getUrlDecoder().decode(password));

        Properties properties = new Properties();
        LDAPUser ldapUser = new LDAPUser();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        properties.put(Context.PROVIDER_URL, ldapURL);
        properties.put(Context.SECURITY_AUTHENTICATION, "simple");
        properties.put(Context.SECURITY_PRINCIPAL, userName);
        properties.put(Context.SECURITY_CREDENTIALS, decodedPassword);
        File fileUsersNotExists = new File(logDiscard);
        FileOutputStream fileOutputStream = checkFileExistence(fileUsersNotExists);
        try {
            InitialDirContext initialDirContext = new InitialDirContext(properties);
            SearchControls searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> answer = initialDirContext.search(baseDN, "(&(objectClass=user)(ENIMatricolaNotes=" + filter + "))", searchControl);

            do {
                SearchResult result = answer.next();
                if (result.getAttributes().get("displayName") == null) {
                    ldapUser.setDisplayName("N/A");
                } else {
                    ldapUser.setDisplayName(result.getAttributes().get("displayName").toString());
                }
                if (result.getAttributes().get("ENIMatricolaNotes") == null) {
                    ldapUser.setENIMatricolaNotes("N/A");
                } else {
                    ldapUser.setENIMatricolaNotes(result.getAttributes().get("ENIMatricolaNotes").toString());
                }
                if (result.getAttributes().get("name") == null) {
                    ldapUser.setName("N/A");
                } else {
                    ldapUser.setName(result.getAttributes().get("name").toString());
                }
                if (result.getAttributes().get("mail") == null) {
                    ldapUser.setMail("N/A");
                } else {
                    ldapUser.setMail(result.getAttributes().get("mail").toString());
                }
                if (result.getAttributes().get("givenName") == null) {
                    ldapUser.setGivenName("N/A");
                } else {
                    ldapUser.setGivenName(result.getAttributes().get("givenName").toString());
                }
                if (result.getAttributes().get("sn") == null) {
                    ldapUser.setSn("N/A");
                } else {
                    ldapUser.setSn(result.getAttributes().get("sn").toString());
                }
                if (result.getAttributes().get("badPwdCount") == null) {
                    ldapUser.setBadPwdCount("N/A");
                } else {
                    ldapUser.setBadPwdCount(result.getAttributes().get("badPwdCount").toString());
                }
                if (result.getAttributes().get("pwdLastSet") == null) {
                    ldapUser.setPwdLastSet("N/A");
                } else {
                    ldapUser.setPwdLastSet(result.getAttributes().get("pwdLastSet").toString());
                }
                if (result.getAttributes().get("userAccountDisabled") == null) {
                    ldapUser.setUserAccountDisabled("N/A");
                } else {
                    ldapUser.setUserAccountDisabled(result.getAttributes().get("userAccountDisabled").toString());
                }
                if (result.getAttributes().get("userDontExpirePassword") == null) {
                    ldapUser.setUserDontExpirePassword("N/A");
                } else {
                    ldapUser.setUserDontExpirePassword(result.getAttributes().get("userDontExpirePassword").toString());
                }
                if (result.getAttributes().get("memberOf") == null) {
                    ldapUser.setMemberOf("N/A");
                } else {
                    ldapUser.setMemberOf(result.getAttributes().get("memberOf").toString());
                }
                if (result.getAttributes().get("ou") == null) {
                    ldapUser.setOu("N/A");
                } else {
                    ldapUser.setOu(result.getAttributes().get("ou").toString());
                }
                userExistsOnLdap.add(ldapUser);
            } while (answer.hasMore());
            initialDirContext.close();
            answer.close();
            fileOutputStream.close();
        } catch (NamingException e) {
            throw new IllegalStateException("An error occurred during user search: " + filter, e);
        }
        return userExistsOnLdap;
    }

    private FileOutputStream checkFileExistence(File fileUsersNotExists) throws IOException {
        FileOutputStream fileOutputStream;
        if (!fileUsersNotExists.exists()) {
            if (fileUsersNotExists.createNewFile()) {
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            } else {
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            }
        } else {
            fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
        }
        return fileOutputStream;
    }

    /**
     * @param userID criterio di ricerca x popolare il DB
     */
    public void searchOnLDAPInsertToDB(String userID, String userRole, String userGroup) throws IOException {
        String decodedPassword = new String(Base64.getUrlDecoder().decode(password));
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        properties.put(Context.PROVIDER_URL, ldapURL);
        properties.put(Context.SECURITY_PRINCIPAL, userName);
        properties.put(Context.SECURITY_CREDENTIALS, decodedPassword);
        File fileUsersNotExists = new File(logDiscard);
        FileOutputStream fileOutputStream = checkFileExistence(fileUsersNotExists);
        try {
            InitialDirContext initialDirContext = new InitialDirContext(properties);
            SearchControls searchControl = new SearchControls();
            searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration<SearchResult> answer = initialDirContext.search(baseDN, "(ENIMatricolaNotes=" + userID + ")", searchControl);
            if (answer.hasMore()) {
                do {
                    SearchResult result = answer.next();
                    if (result.getAttributes().get("displayName") != null) {
                        if (result.getAttributes().get("displayName").toString().contains("'")) {
                            String displayNameReplaced = result.getAttributes().get("displayName").toString().replace("'", "''");
                            QsUser qsUser = new QsUser();
                            qsUser.setUserIsActive("Y");
                            qsUser.setUserid(result.getAttributes().get("ENIMatricolaNotes").get().toString());
                            qsUser.setName(displayNameReplaced);
                            qsUser.setDataLastModify(OffsetDateTime.now());
                            qsUsersService.create(qsUser);
                        } else {
                            QsUser qsUser = new QsUser();
                            qsUser.setUserIsActive("Y");
                            qsUser.setUserid(result.getAttributes().get("ENIMatricolaNotes").get().toString());
                            qsUser.setName(result.getAttributes().get("displayName").toString());
                            qsUser.setDataLastModify(OffsetDateTime.now());
                            qsUsersService.create(qsUser);
                        }
                    } else {
                        QsUser qsUser = new QsUser();
                        qsUser.setUserIsActive("Y");
                        qsUser.setUserid(result.getAttributes().get("ENIMatricolaNotes").toString());
                        qsUser.setName(result.getAttributes().get("displayName").toString());
                        qsUser.setDataLastModify(OffsetDateTime.now());
                        qsUsersService.create(qsUser);
                    }
                    if (result.getAttributes().get("mail") == null) {
                        QsUsersAttrib qsUsersAttrib = new QsUsersAttrib();
                        qsUsersAttrib.setType("email");
                        qsUsersAttrib.setUserid(qsUsersService.findByUserId(result.getAttributes().get("ENIMatricolaNotes").toString()));
                        qsUsersAttrib.setValue("N/A");
                        qsUsersAttributesService.create(qsUsersAttrib, tabAttribute);
                    } else {
                        if (result.getAttributes().get("mail").toString().contains("'")) {
                            String mailReplaced = result.getAttributes().get("mail").toString().replace("'", "''");

                            QsUsersAttrib qsUsersAttrib = new QsUsersAttrib();
                            qsUsersAttrib.setType("email");
                            qsUsersAttrib.setUserid(qsUsersService.findByUserId(result.getAttributes().get("ENIMatricolaNotes").toString()));
                            qsUsersAttrib.setValue(mailReplaced);
                            qsUsersAttributesService.create(qsUsersAttrib, tabAttribute);
                        } else {
                            QsUsersAttrib qsUsersAttrib = new QsUsersAttrib();
                            qsUsersAttrib.setType("email");
                            qsUsersAttrib.setUserid(qsUsersService.findByUserId(result.getAttributes().get("ENIMatricolaNotes").toString()));
                            qsUsersAttrib.setValue(result.getAttributes().get("mail").toString());
                            qsUsersAttributesService.create(qsUsersAttrib, tabAttribute);
                        }
                    }
                    if (result.getAttributes().get("ou") == null) {
                        QsUsersAttrib qsUsersAttrib = new QsUsersAttrib();
                        qsUsersAttrib.setUserid(qsUsersService.findByUserId(result.getAttributes().get("ENIMatricolaNotes").toString()));
                        qsUsersAttrib.setType("organizzazione");
                        qsUsersAttrib.setValue("N/A");
                        qsUsersAttributesService.create(qsUsersAttrib, tabAttribute);
                    } else {
                        QsUsersAttrib qsUsersAttrib = new QsUsersAttrib();
                        qsUsersAttrib.setUserid(qsUsersService.findByUserId(result.getAttributes().get("ENIMatricolaNotes").toString()));
                        qsUsersAttrib.setType("organizzazione");
                        qsUsersAttrib.setValue(result.getAttributes().get("ou").toString());
                        qsUsersAttributesService.create(qsUsersAttrib, tabAttribute);
                    }
                    QsUsersAttrib qsUsersAttrib = new QsUsersAttrib();
                    qsUsersAttrib.setType("gruppo");
                    qsUsersAttrib.setUserid(qsUsersService.findByUserId(result.getAttributes().get("ENIMatricolaNotes").toString()));
                    qsUsersAttrib.setValue(userGroup);
                    qsUsersAttributesService.createOrUpdate(qsUsersAttrib);
                    qsUsersAttrib = new QsUsersAttrib();
                    qsUsersAttrib.setType("ruolo");
                    qsUsersAttrib.setUserid(qsUsersService.findByUserId(result.getAttributes().get("ENIMatricolaNotes").toString()));
                    qsUsersAttrib.setValue(userRole);
                    qsUsersAttributesService.createOrUpdate(qsUsersAttrib);
                } while (answer.hasMore());
            } else {
                String userNotExist = "Utenza non esiste sul VDS: " + userID + "\n";
                userNotExistsOnLdap++;
                fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
                fileOutputStream.write(userNotExist.getBytes());
            }
            initialDirContext.close();
            answer.close();
            fileOutputStream.close();
        } catch (NamingException | IOException e) {
            throw new IllegalStateException("An error occurred during user search: " + userID, e);
        }
    }

    @SneakyThrows
    public List<LDAPUser> findAll() {
        List<LDAPUser> userExistsOnLdap = new ArrayList<>();
        String decodedPassword = new String(Base64.getUrlDecoder().decode(password));
        Properties properties = new Properties();
        LDAPUser ldapUser = new LDAPUser();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);
        properties.put(Context.PROVIDER_URL, ldapURL);
        properties.put(Context.SECURITY_AUTHENTICATION, "simple");
        properties.put(Context.SECURITY_PRINCIPAL, userName);
        properties.put(Context.SECURITY_CREDENTIALS, decodedPassword);
        File fileUsersNotExists = new File(logDiscard);
        FileOutputStream fileOutputStream = checkFileExistence(fileUsersNotExists);
        InitialDirContext initialDirContext = new InitialDirContext(properties);
        SearchControls searchControl = new SearchControls();
        searchControl.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> answer = initialDirContext.search(baseDN, "(&(objectClass=user))", searchControl);
        do {
            SearchResult result = answer.next();
            if (result.getAttributes().get("displayName") == null) {
                ldapUser.setDisplayName("N/A");
            } else {
                ldapUser.setDisplayName(result.getAttributes().get("displayName").toString());
            }
            if (result.getAttributes().get("ENIMatricolaNotes") == null) {
                ldapUser.setENIMatricolaNotes("N/A");
            } else {
                ldapUser.setENIMatricolaNotes(result.getAttributes().get("ENIMatricolaNotes").toString());
            }
            if (result.getAttributes().get("name") == null) {
                ldapUser.setName("N/A");
            } else {
                ldapUser.setName(result.getAttributes().get("name").toString());
            }
            if (result.getAttributes().get("mail") == null) {
                ldapUser.setMail("N/A");
            } else {
                ldapUser.setMail(result.getAttributes().get("mail").toString());
            }
            if (result.getAttributes().get("givenName") == null) {
                ldapUser.setGivenName("N/A");
            } else {
                ldapUser.setGivenName(result.getAttributes().get("givenName").toString());
            }
            if (result.getAttributes().get("sn") == null) {
                ldapUser.setSn("N/A");
            } else {
                ldapUser.setSn(result.getAttributes().get("sn").toString());
            }
            if (result.getAttributes().get("badPwdCount") == null) {
                ldapUser.setBadPwdCount("N/A");
            } else {
                ldapUser.setBadPwdCount(result.getAttributes().get("badPwdCount").toString());
            }
            if (result.getAttributes().get("pwdLastSet") == null) {
                ldapUser.setPwdLastSet("N/A");
            } else {
                ldapUser.setPwdLastSet(result.getAttributes().get("pwdLastSet").toString());
            }
            if (result.getAttributes().get("userAccountDisabled") == null) {
                ldapUser.setUserAccountDisabled("N/A");
            } else {
                ldapUser.setUserAccountDisabled(result.getAttributes().get("userAccountDisabled").toString());
            }
            if (result.getAttributes().get("userDontExpirePassword") == null) {
                ldapUser.setUserDontExpirePassword("N/A");
            } else {
                ldapUser.setUserDontExpirePassword(result.getAttributes().get("userDontExpirePassword").toString());
            }
            if (result.getAttributes().get("memberOf") == null) {
                ldapUser.setMemberOf("N/A");
            } else {
                ldapUser.setMemberOf(result.getAttributes().get("memberOf").toString());
            }
            if (result.getAttributes().get("ou") == null) {
                ldapUser.setOu("N/A");
            } else {
                ldapUser.setOu(result.getAttributes().get("ou").toString());
            }
            userExistsOnLdap.add(ldapUser);
        } while (answer.hasMore());
        initialDirContext.close();
        answer.close();
        fileOutputStream.close();
        return userExistsOnLdap;
    }
}

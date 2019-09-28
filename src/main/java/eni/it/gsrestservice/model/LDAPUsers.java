package eni.it.gsrestservice.model;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;

@Entry(objectClasses = {"person"})
public class LDAPUsers {
    private static final String BASE_DN = "dc=pri";
    @Attribute(name = "displayName")
    String displayName;
    @Attribute(name = "name")
    String name;
    @Attribute(name = "ENIMatricolaNotes")
    String userID;
    @Attribute(name = "mail")
    String mail;
    @Attribute(name = "givenName")
    String givenName;
    @Attribute(name = "sn")
    String sn;
    @Attribute(name = "badPwdCount")
    String badPwdCount;
    @Attribute(name = "pwdLastSet")
    String pwdLastSet;
    @Attribute(name = "msDS-UserAccountDisabled")
    String userAccountDisabled;
    @Attribute(name = "msDS-UserDontExpirePassword")
    String userDontExpirePassword;
    @Attribute(name = "memberOf")
    String memberOf;
    @Attribute(name = "ou")
    String ou;
    @Id
    private Name dn;

    public LDAPUsers() {

    }

    public LDAPUsers(String displayName, String userID, String name, String mail,
                     String givenName, String sn, String badPwdCount, String pwdLastSet,
                     String userAccountDisabled, String userDontExpirePassword, String memberOf, String ou) {
        this.displayName = displayName;
        this.userID = userID;
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

    @Override
    public String toString() {
        return "LDAPUsers{" +
                "displayName='" + displayName + '\'' +
                ", eniMatricolaNotes='" + userID + '\'' +
                ", name='" + name + '\'' +
                ", mail='" + mail + '\'' +
                ", givenName='" + givenName + '\'' +
                ", sn='" + sn + '\'' +
                ", badPwdCount='" + badPwdCount + '\'' +
                ", pwdLastSet='" + pwdLastSet + '\'' +
                ", userAccountDisabled='" + userAccountDisabled + '\'' +
                ", userDontExpirePassword='" + userDontExpirePassword + '\'' +
                ", memberOf='" + memberOf + '\'' +
                ", ou='" + ou + '\'' +
                '}';
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getBadPwdCount() {
        return badPwdCount;
    }

    public void setBadPwdCount(String badPwdCount) {
        this.badPwdCount = badPwdCount;
    }

    public String getPwdLastSet() {
        return pwdLastSet;
    }

    public void setPwdLastSet(String pwdLastSet) {
        this.pwdLastSet = pwdLastSet;
    }

    public String getUserAccountDisabled() {
        return userAccountDisabled;
    }

    public void setUserAccountDisabled(String userAccountDisabled) {
        this.userAccountDisabled = userAccountDisabled;
    }

    public String getUserDontExpirePassword() {
        return userDontExpirePassword;
    }

    public void setUserDontExpirePassword(String userDontExpirePassword) {
        this.userDontExpirePassword = userDontExpirePassword;
    }

    public String getMemberOf() {
        return memberOf;
    }

    public void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }
}

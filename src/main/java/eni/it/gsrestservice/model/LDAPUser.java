package eni.it.gsrestservice.model;

public class LDAPUser {
    private String displayName;
    private String name;
    private String ENIMatricolaNotes;
    private String mail;
    private String givenName;
    private String sn;
    private String badPwdCount;
    private String pwdLastSet;
    private String userAccountDisabled;
    private String userDontExpirePassword;
    private String memberOf;
    private String ou;

    LDAPUser() {

    }

    public LDAPUser(LDAPUser ldapUser) {
        this.displayName = ldapUser.displayName;
        this.ENIMatricolaNotes = ldapUser.ENIMatricolaNotes;
        this.name = ldapUser.name;
        this.mail = ldapUser.mail;
        this.givenName = ldapUser.givenName;
        this.sn = ldapUser.sn;
        this.badPwdCount = ldapUser.badPwdCount;
        this.pwdLastSet = ldapUser.pwdLastSet;
        this.userAccountDisabled = ldapUser.userAccountDisabled;
        this.userDontExpirePassword = ldapUser.userDontExpirePassword;
        this.memberOf = ldapUser.memberOf;
        this.ou = ldapUser.ou;
    }

    public LDAPUser(String displayName, String ENIMatricolaNotes, String name, String mail,
                    String givenName, String sn, String badPwdCount, String pwdLastSet,
                    String userAccountDisabled, String userDontExpirePassword, String memberOf, String ou) {
        this.displayName = displayName;
        this.ENIMatricolaNotes = ENIMatricolaNotes;
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
                ", eniMatricolaNotes='" + ENIMatricolaNotes + '\'' +
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

    void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getENIMatricolaNotes() {
        return ENIMatricolaNotes;
    }

    void setENIMatricolaNotes(String ENIMatricolaNotes) {
        this.ENIMatricolaNotes = ENIMatricolaNotes;
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

    void setMail(String mail) {
        this.mail = mail;
    }

    public String getGivenName() {
        return givenName;
    }

    void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSn() {
        return sn;
    }

    void setSn(String sn) {
        this.sn = sn;
    }

    public String getBadPwdCount() {
        return badPwdCount;
    }

    void setBadPwdCount(String badPwdCount) {
        this.badPwdCount = badPwdCount;
    }

    public String getPwdLastSet() {
        return pwdLastSet;
    }

    void setPwdLastSet(String pwdLastSet) {
        this.pwdLastSet = pwdLastSet;
    }

    public String getUserAccountDisabled() {
        return userAccountDisabled;
    }

    void setUserAccountDisabled(String userAccountDisabled) {
        this.userAccountDisabled = userAccountDisabled;
    }

    public String getUserDontExpirePassword() {
        return userDontExpirePassword;
    }

    void setUserDontExpirePassword(String userDontExpirePassword) {
        this.userDontExpirePassword = userDontExpirePassword;
    }

    public String getMemberOf() {
        return memberOf;
    }

    void setMemberOf(String memberOf) {
        this.memberOf = memberOf;
    }

    public String getOu() {
        return ou;
    }

    void setOu(String ou) {
        this.ou = ou;
    }
}

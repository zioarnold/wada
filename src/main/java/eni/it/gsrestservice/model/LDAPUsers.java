package eni.it.gsrestservice.model;

public class LDAPUsers {
    private String displayName;
    private String eniMatricolaNotes;
    private String name;
    private String mail;
    private String givenName;
    private String sn;
    private String badPwdCount;
    private String pwdLastSet;
    private String userAccountDisabled;
    private String userDontExpirePassword;
    private String memberOf;
    private String ou;

    public LDAPUsers() {

    }

    public LDAPUsers(String displayName, String eniMatricolaNotes, String name, String mail,
                     String givenName, String sn, String badPwdCount, String pwdLastSet,
                     String userAccountDisabled, String userDontExpirePassword, String memberOf, String ou) {
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

    @Override
    public String toString() {
        return "LDAPUsers{" +
                "displayName='" + displayName + '\'' +
                ", eniMatricolaNotes='" + eniMatricolaNotes + '\'' +
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

    public String getEniMatricolaNotes() {
        return eniMatricolaNotes;
    }

    public void setEniMatricolaNotes(String eniMatricolaNotes) {
        this.eniMatricolaNotes = eniMatricolaNotes;
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

package eni.it.gsrestservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}

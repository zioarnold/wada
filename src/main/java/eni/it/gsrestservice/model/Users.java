package eni.it.gsrestservice.model;


import javax.persistence.*;

@Entity(name = "users_1")
@Table(name = "USERS_1")
public class Users {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;

    @Column(name = "USERID")
    private String userID;
    @Column(name = "USER_TYPE")
    private String userType;
    @Column(name = "USER_GROUP")
    private String userGroup;
    @Column(name = "USER_IS_ACTIVE")
    private String userActive;
    @Column(name = "ORGANIZZAZIONE")
    private String organization;
    @Column(name = "EMAIL")
    private String email;

    protected Users() {

    }

    public Users(String userID, String userType, String userGroup, String userActive, String organization, String email) {
        this.userID = userID;
        this.userType = userType;
        this.userGroup = userGroup;
        this.userActive = userActive;
        this.organization = organization;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getUserActive() {
        return userActive;
    }

    public void setUserActive(String userActive) {
        this.userActive = userActive;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", userID='" + userID + '\'' +
                ", userType='" + userType + '\'' +
                ", userGroup='" + userGroup + '\'' +
                ", userActive='" + userActive + '\'' +
                ", organization='" + organization + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

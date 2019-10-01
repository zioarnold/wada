package eni.it.gsrestservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "q_users_attrib")
@Table(name = "Q_USERS_ATTRIB", schema = "WADA")
public class QUsersAttribute {
    @Id
    @Column(name = "USERID")
    private String userID;

    @Column(name = "TYPE")
    private String type;
    @Column(name = "VALUE")
    private String value;

    protected QUsersAttribute() {

    }

    public QUsersAttribute(String userID, String type, String value) {
        this.userID = userID;
        this.type = type;
        this.value = value;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "QUsersAttribute{" +
                "userID='" + userID + '\'' +
                ", type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

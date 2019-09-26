package eni.it.gsrestservice.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "u_users")
public class FarmQSense implements Serializable {
    @Id
    private String userID; //matricola
    private String came; //came
    private String note; //note
    private String serviceLevel; //livello di servizio
    private String description; //descrizione

    public FarmQSense() {

    }

    public FarmQSense(String userID, String came, String note, String serviceLevel, String description) {
        this.userID = userID;
        this.came = came;
        this.note = note;
        this.serviceLevel = serviceLevel;
        this.description = description;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCame() {
        return came;
    }

    public void setCame(String came) {
        this.came = came;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FarmQSense{" +
                "userID='" + userID + '\'' +
                ", came='" + came + '\'' +
                ", note='" + note + '\'' +
                ", serviceLevel='" + serviceLevel + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

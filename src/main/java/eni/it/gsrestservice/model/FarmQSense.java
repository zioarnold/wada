package eni.it.gsrestservice.model;

import javax.persistence.*;

@Entity(name = "farm_qsense")
@Table(name = "FARM_QSENSE", schema = "WADA")
public class FarmQSense {
    @Id
    @Column(name = "FARMID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer farmID;
    @Column(name = "USERID")
    private String userID; //matricola
    @Column(name = "CAME")
    private Integer came; //came
    @Column(name = "DESCRIZIONE")
    private String description; //descrizione
    @Column(name = "NOTE")
    private String note; //note
    @Column(name = "LIVELLO_SERVIZIO")
    private String serviceLevel; //livello di servizio

    public FarmQSense() {

    }

    public FarmQSense(Integer farmID, String userID, Integer came, String note, String serviceLevel, String description) {
        this.farmID = farmID;
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

    public Integer getCame() {
        return came;
    }

    public void setCame(Integer came) {
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

    public Integer getFarmID() {
        return farmID;
    }

    public void setFarmID(Integer farmID) {
        this.farmID = farmID;
    }

    @Override
    public String toString() {
        return "FarmQSense{" +
                "farmID=" + farmID +
                ", userID='" + userID + '\'' +
                ", came=" + came +
                ", description='" + description + '\'' +
                ", note='" + note + '\'' +
                ", serviceLevel='" + serviceLevel + '\'' +
                '}';
    }
}

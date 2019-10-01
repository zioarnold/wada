package eni.it.gsrestservice.model;

public class QUsers {
    private String userId;
    private String came;
    private String description;
    private String serviceLevel;
    private String name;
    private String note;
    private String type;
    private String value;
    private String userType;
    private String userGroup;
    private String userIsActive;
    private String organization;
    private String email;

    QUsers() {

    }

    QUsers(QUsers users) {
        this.userId = users.userId;
        this.came = users.came;
        this.description = users.description;
        this.serviceLevel = users.serviceLevel;
        this.name = users.name;
        this.note = users.note;
//        this.type = users.type;
//        this.value = users.value;
        this.userType = users.userType;
        this.userGroup = users.userGroup;
        this.userIsActive = users.userIsActive;
        this.organization = users.organization;
        this.email = users.email;
    }

    public QUsers(String userId, String came, String description, String serviceLevel,
                  String name, String note,
//                  String type,
//                  String value,
                  String userType,
                  String userGroup, String userIsActive, String organization, String email) {
        this.userId = userId;
        this.came = came;
        this.description = description;
        this.serviceLevel = serviceLevel;
        this.name = name;
        this.note = note;
//        this.type = type;
//        this.value = value;
        this.userType = userType;
        this.userGroup = userGroup;
        this.userIsActive = userIsActive;
        this.organization = organization;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCame() {
        return came;
    }

    public void setCame(String came) {
        this.came = came;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getUserIsActive() {
        return userIsActive;
    }

    public void setUserIsActive(String userIsActive) {
        this.userIsActive = userIsActive;
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
}

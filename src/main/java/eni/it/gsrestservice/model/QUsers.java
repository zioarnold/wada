package eni.it.gsrestservice.model;

public class QUsers {
    private String userId;
    private String name;
    private String userType;
    private String userValue;
    private String userIsActive;
    private String type;
    private String value;

    QUsers() {

    }

    QUsers(QUsers users) {
        this.userId = users.userId;
        this.name = users.name;
        this.userType = users.userType;
        this.userValue = users.userValue;
        this.userIsActive = users.userIsActive;
        this.type = users.type;
        this.value = users.value;
    }

    public QUsers(String userId,
                  String name,
                  String userIsActive
    ) {
        this.userId = userId;
        this.name = name;
        this.userIsActive = userIsActive;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserValue() {
        return userValue;
    }

    public void setUserValue(String userValue) {
        this.userValue = userValue;
    }

    public String getUserIsActive() {
        return userIsActive;
    }

    public void setUserIsActive(String userIsActive) {
        this.userIsActive = userIsActive;
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
}

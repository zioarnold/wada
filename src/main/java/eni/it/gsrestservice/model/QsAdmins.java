package eni.it.gsrestservice.model;

public class QsAdmins {
    private int id;
    private String username;
    private String password;
    private String currentSessionLoginTime;
    private String sessionLoginExpireTime;
    private String auth;
    private String role;

    public QsAdmins() {

    }

    public QsAdmins(int id, String username,
                    String password,
                    String currentSessionLoginTime,
                    String sessionLoginExpireTime,
                    String auth,
                    String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.currentSessionLoginTime = currentSessionLoginTime;
        this.sessionLoginExpireTime = sessionLoginExpireTime;
        this.auth = auth;
        this.role = role;
    }

    public QsAdmins(QsAdmins qsAdmins) {
        this.id = qsAdmins.id;
        this.username = qsAdmins.username;
        this.password = qsAdmins.password;
        this.currentSessionLoginTime = qsAdmins.currentSessionLoginTime;
        this.sessionLoginExpireTime = qsAdmins.sessionLoginExpireTime;
        this.auth = qsAdmins.auth;
        this.role = qsAdmins.role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentSessionLoginTime() {
        return currentSessionLoginTime;
    }

    public void setCurrentSessionLoginTime(String currentSessionLoginTime) {
        this.currentSessionLoginTime = currentSessionLoginTime;
    }

    public String getSessionLoginExpireTime() {
        return sessionLoginExpireTime;
    }

    public void setSessionLoginExpireTime(String sessionLoginExpireTime) {
        this.sessionLoginExpireTime = sessionLoginExpireTime;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "QsAdmins{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", currentSessionLoginTime='" + currentSessionLoginTime + '\'' +
                ", sessionLoginExpireTime='" + sessionLoginExpireTime + '\'' +
                ", auth=" + auth +
                ", role='" + role + '\'' +
                '}';
    }
}

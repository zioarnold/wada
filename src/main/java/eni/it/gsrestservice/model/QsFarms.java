package eni.it.gsrestservice.model;

public class QsFarms {
    private String farmId;
    private String description;
    private String came;
    private String dbHost;
    private String dbUser;
    private String dbPassword;
    private String dbPort;
    private String dbSid;
    private String qsHost;
    private String qsHeader;
    private String qsPathClientJKS;
    private String qsPathRootJKS;
    private String qsKeyStorePwd;
    private String qsXrfKey;
    private String qsReloadTaskName;
    private String note;
    private String environment;

    public QsFarms() {

    }

    QsFarms(String farmId, String description, String came, String dbHost, String dbUser, String dbPassword,
            String dbPort, String dbSid, String qsHost, String qsHeader, String qsPathClientJKS,
            String qsPathRootJKS, String qsKeyStorePwd, String qsXrfKey, String qsReloadTaskName,
            String note, String environment) {
        this.farmId = farmId;
        this.description = description;
        this.came = came;
        this.dbHost = dbHost;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.dbPort = dbPort;
        this.dbSid = dbSid;
        this.qsHost = qsHost;
        this.qsHeader = qsHeader;
        this.qsPathClientJKS = qsPathClientJKS;
        this.qsPathRootJKS = qsPathRootJKS;
        this.qsKeyStorePwd = qsKeyStorePwd;
        this.qsXrfKey = qsXrfKey;
        this.qsReloadTaskName = qsReloadTaskName;
        this.note = note;
        this.environment = environment;
    }

    public String getQsHeader() {
        return qsHeader;
    }

    void setQsHeader(String qsHeader) {
        this.qsHeader = qsHeader;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCame() {
        return came;
    }

    void setCame(String came) {
        this.came = came;
    }

    public String getDbUser() {
        return dbUser;
    }

    void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbPort() {
        return dbPort;
    }

    void setDbPort(String dbPort) {
        this.dbPort = dbPort;
    }

    public String getDbSid() {
        return dbSid;
    }

    void setDbSid(String dbSid) {
        this.dbSid = dbSid;
    }

    public String getQsHost() {
        return qsHost;
    }

    void setQsHost(String qsHost) {
        this.qsHost = qsHost;
    }

    public String getQsPathClientJKS() {
        return qsPathClientJKS;
    }

    void setQsPathClientJKS(String qsPathClientJKS) {
        this.qsPathClientJKS = qsPathClientJKS;
    }

    public String getQsPathRootJKS() {
        return qsPathRootJKS;
    }

    void setQsPathRootJKS(String qsPathRootJKS) {
        this.qsPathRootJKS = qsPathRootJKS;
    }

    public String getQsKeyStorePwd() {
        return qsKeyStorePwd;
    }

    void setQsKeyStorePwd(String qsKeyStorePwd) {
        this.qsKeyStorePwd = qsKeyStorePwd;
    }

    public String getQsXrfKey() {
        return qsXrfKey;
    }

    void setQsXrfKey(String qsXrfKey) {
        this.qsXrfKey = qsXrfKey;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFarmId() {
        return farmId;
    }

    void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getDbHost() {
        return dbHost;
    }

    void setDbHost(String dbHost) {
        this.dbHost = dbHost;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getQsReloadTaskName() {
        return qsReloadTaskName;
    }

    public void setQsReloadTaskName(String qsReloadTaskName) {
        this.qsReloadTaskName = qsReloadTaskName;
    }

    @Override
    public String toString() {
        return "QsFarms{" +
                "farmId='" + farmId + '\'' +
                ", description='" + description + '\'' +
                ", came='" + came + '\'' +
                ", dbHost='" + dbHost + '\'' +
                ", dbUser='" + dbUser + '\'' +
                ", dbPassword='" + dbPassword + '\'' +
                ", dbPort='" + dbPort + '\'' +
                ", dbSid='" + dbSid + '\'' +
                ", qsHost='" + qsHost + '\'' +
                ", qsHeader='" + qsHeader + '\'' +
                ", qsPathClientJKS='" + qsPathClientJKS + '\'' +
                ", qsPathRootJKS='" + qsPathRootJKS + '\'' +
                ", qsKeyStorePwd='" + qsKeyStorePwd + '\'' +
                ", qsXrfKey='" + qsXrfKey + '\'' +
                ", qsReloadTaskName='" + qsReloadTaskName + '\'' +
                ", note='" + note + '\'' +
                ", environment='" + environment + '\'' +
                '}';
    }
}

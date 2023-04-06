package eni.it.gsrestservice.model;

import eni.it.gsrestservice.config.LoggingMisc;
import oracle.jdbc.driver.OracleDriver;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SqlResolve")
public class DBConnectionOperationCentralized {
    private static String hostname;
    private static String port;
    private static String sid;
    private static String username;
    private static String password;
    private static Statement statement; //oggetto per effettuare le query
    private static Connection connection; //oggetto che conterra' la stringa di connessione
    private LoggingMisc loggingMisc; //oggetto di log
    private ResultSet resultSet; //oggetto per restituire risultato dallo statement
    private static String qsAdminUsers; //tbl QSADMINUSERS
    private static String qsFarms;//tbl QSFARMS
    private final List<QsFarms> farmsList;
    private final List<QsAdmins> adminUsersList;
    private final List<QsAudit> qsAuditList;
    private static boolean isAuthenticated;

    public DBConnectionOperationCentralized() {
        loggingMisc = new LoggingMisc();
        farmsList = new ArrayList<>();
        adminUsersList = new ArrayList<>();
        qsAuditList = new ArrayList<>();
        statement = null;
        resultSet = null;
    }

    public void initDB(
            String hostname,
            String port,
            String sid,
            String username,
            String password,
            String qsAdminUsers,
            String qsFarms) {
        DBConnectionOperationCentralized.hostname = hostname;
        DBConnectionOperationCentralized.port = port;
        DBConnectionOperationCentralized.sid = sid;
        DBConnectionOperationCentralized.username = username;
        DBConnectionOperationCentralized.password = password;
        DBConnectionOperationCentralized.qsAdminUsers = qsAdminUsers;
        DBConnectionOperationCentralized.qsFarms = qsFarms;
    }

    public void updateAudit(String query) {
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - " + query);
                resultSet = statement.executeQuery(query);
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
    }

    void connectDBORA() {
        loggingMisc = new LoggingMisc();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Initializing OracleSQL Driver");
            DriverManager.registerDriver(new OracleDriver());
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Initializing OracleSQL Driver Successful");
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Initializing connection to DB");
            setConnection(DriverManager.getConnection(hostname + ":" + port + ":" + sid, username, password));
        } catch (SQLException e) {
            e.printStackTrace();
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Failed to connect: " + e.getSQLState()
                    + "Connection is " + getConnection());
        }
    }

    void disconnectDBORA() {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if resultSet is not null");
        if (resultSet != null) {
            try {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Trying to close resultSet");
                resultSet.close();
                if (resultSet.isClosed()) {
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Closing resultSet successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resultSet = null;
        }
        loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if statement is not null");
        if (statement != null) {
            try {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Trying to close statement");
                statement.close();
                if (statement.isClosed()) {
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Closing statement successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement = null;
        }
        loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is not null");
        if (connection != null) {
            try {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Trying to close connection");
                connection.close();
                if (connection.isClosed()) {
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Closing connection successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
        try {
            DriverManager.deregisterDriver(new OracleDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean logout(String username) {
        connectDBORA();
        isAuthenticated = true;
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - select USERNAME, PASSWORD from " + qsAdminUsers + " where USERNAME like '" + username + "' and PASSWORD like '" + MD5(password) + "'");
                resultSet = statement.executeQuery("select USERNAME, PASSWORD from " + qsAdminUsers + " where USERNAME like '" + username + "' and PASSWORD like '" + MD5(password) + "'");
                while (resultSet.next()) {
                    QsAdminUsers.username = resultSet.getString("USERNAME");
                    QsAdminUsers.password = resultSet.getString("PASSWORD");
                }
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + "- update " + qsAdminUsers + " set AUTHENTICATED = 'N' where USERNAME like '" + username + "'");
                resultSet = statement.executeQuery("update " + qsAdminUsers + " set AUTHENTICATED = 'N' where USERNAME like '" + username + "'");
                isAuthenticated = false;
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isAuthenticated;
    }

    public boolean login(String username, String password) {
        connectDBORA();
        isAuthenticated = false;
        int index = 0;
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - select USERNAME, PASSWORD from " + qsAdminUsers + " where USERNAME like '" + username + "' and PASSWORD like '" + MD5(password) + "'");
                resultSet = statement.executeQuery("select USERNAME, PASSWORD, ROLE from " + qsAdminUsers + " where USERNAME like '" + username + "' and PASSWORD like '" + MD5(password) + "'");

                while (resultSet.next()) {
                    ++index;
                    QsAdminUsers.username = resultSet.getString("USERNAME");
                    QsAdminUsers.password = resultSet.getString("PASSWORD");
                    QsAdminUsers.role = resultSet.getString("ROLE");
                }

                if (index > 0) {
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + "- update " + qsAdminUsers + " set CURRENT_SESSION_LOGIN_TIME = sysdate, SESSION_LOGIN_EXPIRE_TIME = sysdate + (60/1440), AUTHENTICATED = 'Y' where USERNAME like '" + username + "' and PASSWORD like '" + MD5(password) + "'");
                    resultSet = statement.executeQuery("update " + qsAdminUsers + " set CURRENT_SESSION_LOGIN_TIME = sysdate, SESSION_LOGIN_EXPIRE_TIME = sysdate + (60/1440), AUTHENTICATED = 'Y' where USERNAME like '" + username + "' and PASSWORD like '" + MD5(password) + "'");
                    isAuthenticated = true;
                } else {
                    isAuthenticated = false;
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isAuthenticated;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        DBConnectionOperationCentralized.connection = connection;
    }

    public List<QsAudit> report() {
        qsAuditList.clear();
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - select ID, DESCRIPTION, EXECUTION_DATA FROM QSAUDITLOG");
                resultSet = statement.executeQuery("select ID, DESCRIPTION, EXECUTION_DATA FROM QSAUDITLOG");
                while (resultSet.next()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    String execution_data = simpleDateFormat.format(resultSet.getDate("EXECUTION_DATA"));
                    QsAudit qsAudit = new QsAudit();
                    qsAudit.setId(resultSet.getInt("ID"));
                    qsAudit.setLog(resultSet.getString("DESCRIPTION"));
                    qsAudit.setDate(execution_data);
                    qsAuditList.add(qsAudit);
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        return qsAuditList;
    }

    public boolean selectFarm(String farmName) {
        connectDBORA();
        boolean isSelected = false;
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query: \n"
                        + "SELECT FARM ID, DESCRIZIONE, CAME, DBUSER, DBPASSWORD, DBHOST, DBPORT, DBSID " +
                        "QSHOST, QSPATHCLIENT, QSPATHROOT, QSXRFKEY, QSKSPASSWD, NOTE, " +
                        "QSUSERHEADER, ENVIRONMENT, QSRELOADTASKNAME FROM " + qsFarms + " WHERE DESCRIZIONE LIKE " + farmName);
                resultSet = statement.executeQuery("select FARMID, " +
                        "       DESCRIZIONE, " +
                        "       CAME, " +
                        "       DBUSER, " +
                        "       DBPASSWORD, " +
                        "       DBHOST, " +
                        "       DBPORT, " +
                        "       DBSID, " +
                        "       QSHOST, " +
                        "       QSPATHCLIENT, " +
                        "       QSPATHROOT, " +
                        "       QSXRFKEY, " +
                        "       QSKSPASSWD, " +
                        "       NOTE, " +
                        "       QSUSERHEADER, " +
                        "       ENVIRONMENT, " +
                        "       QSRELOADTASKNAME " +
                        "FROM " + qsFarms + " " +
                        "WHERE DESCRIZIONE LIKE '" + farmName + "'");
                while (resultSet.next()) {
                    Farm.farmId = resultSet.getString("FARMID");
                    Farm.description = resultSet.getString("DESCRIZIONE");
                    Farm.came = resultSet.getString("CAME");
                    Farm.dbUser = resultSet.getString("DBUSER");
                    Farm.dbPassword = resultSet.getString("DBPASSWORD");
                    Farm.dbHost = resultSet.getString("DBHOST");
                    Farm.dbPort = resultSet.getString("DBPORT");
                    Farm.dbSid = resultSet.getString("DBSID");
                    Farm.qsHost = resultSet.getString("QSHOST");
                    Farm.qsHeader = resultSet.getString("QSUSERHEADER");
                    Farm.qsPathClientJKS = resultSet.getString("QSPATHCLIENT");
                    Farm.qsPathRootJKS = resultSet.getString("QSPATHROOT");
                    Farm.qsKeyStorePwd = resultSet.getString("QSKSPASSWD");
                    Farm.qsXrfKey = resultSet.getString("QSXRFKEY");
                    Farm.note = resultSet.getString("NOTE");
                    Farm.environment = resultSet.getString("ENVIRONMENT");
                    Farm.qsReloadTaskName = resultSet.getString("QSRELOADTASKNAME");
                }
                isSelected = true;
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isSelected;
    }

    public boolean initConnector() {
        connectDBORA();
        boolean isInitiated = false;
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                resultSet = statement.executeQuery("select FARMID, DESCRIZIONE, CAME, DBUSER, DBPASSWORD, DBHOST, DBPORT, DBSID, QSHOST, QSPATHCLIENT," +
                        "QSPATHROOT, QSXRFKEY, QSKSPASSWD, NOTE, QSUSERHEADER, ENVIRONMENT, QSRELOADTASKNAME FROM " + qsFarms);
                while (resultSet.next()) {
                    Farm.dbUser = resultSet.getString("DBUSER");
                    Farm.dbPassword = resultSet.getString("DBPASSWORD");
                    Farm.dbHost = resultSet.getString("DBHOST");
                    Farm.dbPort = resultSet.getString("DBPORT");
                    Farm.dbSid = resultSet.getString("DBSID");
                    Farm.qsHost = resultSet.getString("QSHOST");
                    Farm.qsHeader = resultSet.getString("QSUSERHEADER");
                    Farm.qsPathClientJKS = resultSet.getString("QSPATHCLIENT");
                    Farm.qsPathRootJKS = resultSet.getString("QSPATHROOT");
                    Farm.qsKeyStorePwd = resultSet.getString("QSKSPASSWD");
                    Farm.qsXrfKey = resultSet.getString("QSXRFKEY");
                    Farm.qsReloadTaskName = resultSet.getString("QSRELOADTASKNAME");
                }
                isInitiated = true;
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isInitiated;
    }

    public int checkSession(String username) {
        connectDBORA();
        int isSessionExpired = 0;
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - select CURRENT_SESSION_LOGIN_TIME, SESSION_LOGIN_EXPIRE_TIME from " + qsAdminUsers + " where USERNAME like '" + username + "'");
                resultSet = statement.executeQuery("select CURRENT_SESSION_LOGIN_TIME, SESSION_LOGIN_EXPIRE_TIME, AUTHENTICATED from " + qsAdminUsers + " where USERNAME like '" + username + "'");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date currentTime = new Date(System.currentTimeMillis());
                String format = simpleDateFormat.format(currentTime);
                while (resultSet.next()) {
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                    String session_login_expire_time = simpleDateFormat1.format(resultSet.getDate("SESSION_LOGIN_EXPIRE_TIME"));
                    isSessionExpired = format.compareTo(session_login_expire_time);
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isSessionExpired;
    }

    public String MD5(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createAdmin(String username, String password, String role) {
        boolean isCreated = false, isAuditQueryCreated = false;
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - insert into " + qsAdminUsers + " (USERNAME, PASSWORD, ROLE) VALUES ('" + username + "','" + MD5(password) + "','" + role + "')");
                resultSet = statement.executeQuery("insert into " + qsAdminUsers + " (USERNAME, PASSWORD, ROLE) VALUES ('" + username + "','" + MD5(password) + "','" + role + "')");
                isCreated = resultSet.next();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }

        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - insert into QSAUDITLOG (DESCRIPTION) VALUE ('Utenza " + QsAdminUsers.username + " ha eseguito la seguente query: insert into " + qsAdminUsers + "(USERNAME, PASSWORD, ROLE) VALUES (" + username + "," + MD5(password) + "," + role + "')");
                resultSet = statement.executeQuery("INSERT INTO QSAUDITLOG (DESCRIPTION) VALUES ('Utenza " + QsAdminUsers.username + " ha eseguito la seguente query: insert into " + qsAdminUsers + "(USERNAME, PASSWORD, ROLE) VALUES (" + username + "," + MD5(password) + "," + role + ")')");
                isAuditQueryCreated = resultSet.next();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isCreated || isAuditQueryCreated;
    }

    public static boolean isIsAuthenticated() {
        return isAuthenticated;
    }

    public static void setIsAuthenticated(boolean isAuthenticated) {
        DBConnectionOperationCentralized.isAuthenticated = isAuthenticated;
    }

    public boolean addNewFarm(String description, String dbUser, String dbPassword, String dbHost,
                              String qsHost, String qsPathClient, String qsPathRoot, String qsXrfKey, String qsKsPassword,
                              String note, String dbSid, String dbPort, String qsUserHeader, String environment, String came) {
        boolean isCreated = false;
        try {
            connectDBORA();
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Looking for the FARM by : " + came);
                resultSet = statement.executeQuery("select count(CAME) from " + qsFarms + " where CAME like '" + came + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Found 0 farm by came: " + came);
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - insert into " + qsFarms + " (DESCRIZIONE, DBUSER, DBPASSWORD, DBHOST, QSHOST, QSPATHCLIENT, QSPATHROOT, QSXRFKEY, QSKSPASSWD, NOTE, DBSID, DBPORT, QSUSERHEADER, CAME, ENVIRONMENT) " +
                            "VALUES ('" + description + "', '" + dbUser + "','" + dbPassword + "','" + dbHost + "','" + qsHost + "','" + qsPathClient + "','" + qsPathRoot + "','" + qsXrfKey + "','" + qsKsPassword + "','" + note + "','" + dbSid + "','" + dbPort + "','" + qsUserHeader + "','" + came + "','" + environment + "')");
                    resultSet = statement.executeQuery("insert into " + qsFarms + " (DESCRIZIONE, DBUSER, DBPASSWORD, DBHOST, QSHOST, QSPATHCLIENT, QSPATHROOT, QSXRFKEY, QSKSPASSWD, NOTE, DBSID, DBPORT, QSUSERHEADER, CAME, ENVIRONMENT) " +
                            "VALUES ('" + description + "', '" + dbUser + "','" + dbPassword + "','" + dbHost + "','" + qsHost + "','" + qsPathClient + "','" + qsPathRoot + "','" + qsXrfKey + "','" + qsKsPassword + "','" + note + "','" + dbSid + "','" + dbPort + "','" + qsUserHeader + "','" + came + "','" + environment + "')");
                    isCreated = resultSet.next();
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - insert into " + qsFarms + " (DESCRIZIONE, DBUSER, DBPASSWORD, DBHOST, QSHOST, QSPATHCLIENT, QSPATHROOT, QSXRFKEY, QSKSPASSWD, NOTE, DBSID, DBPORT, QSUSERHEADER, CAME, ENVIRONMENT) " +
                            "VALUES ('" + description + "', '" + dbUser + "','" + dbPassword + "','" + dbHost + "','" + qsHost + "','" + qsPathClient + "','" + qsPathRoot + "','" + qsXrfKey + "','" + qsKsPassword + "','" + note + "','" + dbSid + "','" + dbPort + "','" + qsUserHeader + "','" + came + "','" + environment + "') - successful");
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Something went wrong");
                }
            }
        } catch (SQLException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + ex.getLocalizedMessage());
        }
        disconnectDBORA();
        return isCreated;
    }

    public List<QsFarms> getAllFarms() {
        connectDBORA();
        farmsList.clear();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                resultSet = statement.executeQuery("select FARMID, DESCRIZIONE, CAME, DBUSER, DBPASSWORD, DBHOST, DBPORT, DBSID, QSHOST, QSPATHCLIENT," +
                        "QSPATHROOT, QSXRFKEY, QSKSPASSWD, NOTE, QSUSERHEADER, ENVIRONMENT , QSRELOADTASKNAME FROM " + qsFarms);
                while (resultSet.next()) {
                    QsFarms qsFarms = new QsFarms(resultSet.getString("FARMID"),
                            resultSet.getString("DESCRIZIONE"),
                            resultSet.getString("CAME"),
                            resultSet.getString("DBHOST"),
                            resultSet.getString("DBUSER"),
                            resultSet.getString("DBPASSWORD"),
                            resultSet.getString("DBPORT"),
                            resultSet.getString("DBSID"),
                            resultSet.getString("QSHOST"),
                            resultSet.getString("QSUSERHEADER"),
                            resultSet.getString("QSPATHCLIENT"),
                            resultSet.getString("QSPATHROOT"),
                            resultSet.getString("QSKSPASSWD"),
                            resultSet.getString("QSXRFKEY"),
                            resultSet.getString("QSRELOADTASKNAME"),
                            resultSet.getString("NOTE"),
                            resultSet.getString("ENVIRONMENT"));
                    farmsList.add(qsFarms);
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return farmsList;
    }

    public List<QsAdmins> getAllAdmins() {
        adminUsersList.clear();
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                resultSet = statement.executeQuery("select ID, USERNAME, PASSWORD, CURRENT_SESSION_LOGIN_TIME, SESSION_LOGIN_EXPIRE_TIME, AUTHENTICATED, ROLE FROM " + qsAdminUsers);
                while (resultSet.next()) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String session_login_expire_time = simpleDateFormat.format(resultSet.getDate("SESSION_LOGIN_EXPIRE_TIME"));
                    String current_session_login_time = simpleDateFormat.format(resultSet.getDate("CURRENT_SESSION_LOGIN_TIME"));
                    QsAdmins qsAdminUsers = new QsAdmins(resultSet.getInt("ID"),
                            resultSet.getString("USERNAME"),
                            resultSet.getString("PASSWORD"),
                            current_session_login_time,
                            session_login_expire_time,
                            resultSet.getString("AUTHENTICATED"),
                            resultSet.getString("ROLE"));
                    adminUsersList.add(qsAdminUsers);
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return adminUsersList;
    }

    public List<QsFarms> getFarmDataById(String farmId) {
        connectDBORA();
        farmsList.clear();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                resultSet = statement.executeQuery("select FARMID, DESCRIZIONE, CAME, DBUSER, DBPASSWORD, DBHOST, DBPORT, DBSID, QSHOST, QSPATHCLIENT," +
                        "QSPATHROOT, QSXRFKEY, QSKSPASSWD, NOTE, QSUSERHEADER, ENVIRONMENT, QSRELOADTASKNAME FROM " + qsFarms + " WHERE FARMID LIKE '" + farmId + "'");
                while (resultSet.next()) {
                    QsFarms qsFarms = new QsFarms(resultSet.getString("FARMID"),
                            resultSet.getString("DESCRIZIONE"),
                            resultSet.getString("CAME"),
                            resultSet.getString("DBHOST"),
                            resultSet.getString("DBUSER"),
                            resultSet.getString("DBPASSWORD"),
                            resultSet.getString("DBPORT"),
                            resultSet.getString("DBSID"),
                            resultSet.getString("QSHOST"),
                            resultSet.getString("QSUSERHEADER"),
                            resultSet.getString("QSPATHCLIENT"),
                            resultSet.getString("QSPATHROOT"),
                            resultSet.getString("QSKSPASSWD"),
                            resultSet.getString("QSXRFKEY"),
                            resultSet.getString("QSRELOADTASKNAME"),
                            resultSet.getString("NOTE"),
                            resultSet.getString("ENVIRONMENT"));
                    farmsList.add(qsFarms);
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return farmsList;
    }

    public List<QsAdmins> getAdminUserDataById(String adminId) {
        connectDBORA();
        adminUsersList.clear();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                resultSet = statement.executeQuery("select id, username, password, role from " + qsAdminUsers + " where id like '" + adminId + "'");
                while (resultSet.next()) {
                    QsAdmins qsAdmins = new QsAdmins(
                            resultSet.getInt("ID"),
                            resultSet.getString("USERNAME"),
                            resultSet.getString("PASSWORD"),
                            "",
                            "",
                            "",
                            resultSet.getString("ROLE")
                    );
                    adminUsersList.add(qsAdmins);
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return adminUsersList;
    }

    public boolean updateFarm(String farmId, String description, String dbUser, String dbPassword, String dbHost,
                              String qsHost, String qsReloadTaskName, String qsPathClient, String qsPathRoot, String qsXrfKey,
                              String qsKsPassword, String note, String dbSid, String dbPort,
                              String qsUserHeader, String environment, String came) {
        boolean isUpdated = false;
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - update " + qsFarms + "\n" +
                        "set DESCRIZIONE    ='" + description + " ',\n" +
                        "    DBUSER         ='" + dbUser + " ',\n" +
                        "    DBPASSWORD     ='" + dbPassword + " ',\n" +
                        "    DBHOST         ='" + dbHost + " ',\n" +
                        "    QSHOST         ='" + qsHost + " ',\n" +
                        "    QSRELOADTASKNAME         ='" + qsReloadTaskName + " ',\n" +
                        "    QSPATHCLIENT   ='" + qsPathClient + " ',\n" +
                        "    QSPATHROOT     ='" + qsPathRoot + " ',\n" +
                        "    QSXRFKEY       ='" + qsXrfKey + " ',\n" +
                        "    QSKSPASSWD     ='" + qsKsPassword + " ',\n" +
                        "    NOTE           ='" + note + " ',\n" +
                        "    DBSID          ='" + dbSid + " ',\n" +
                        "    DBPORT         ='" + dbPort + " ',\n" +
                        "    QSUSERHEADER   ='" + qsUserHeader + " ',\n" +
                        "    ENVIRONMENT    ='" + environment + " ',\n" +
                        "    CAME           ='" + came + " ',\n" +
                        "    DATALASTMODIFY = SYSDATE\n" +
                        "WHERE FARMID like '" + farmId + "';");
                resultSet = statement.executeQuery("update " + qsFarms + " " +
                        "set DESCRIZIONE    ='" + description + "'," +
                        "    DBUSER         ='" + dbUser + "'," +
                        "    DBPASSWORD     ='" + dbPassword + "'," +
                        "    DBHOST         ='" + dbHost + "'," +
                        "    QSHOST         ='" + qsHost + "'," +
                        "    QSRELOADTASKNAME         ='" + qsReloadTaskName + "'," +
                        "    QSPATHCLIENT   ='" + qsPathClient + "'," +
                        "    QSPATHROOT     ='" + qsPathRoot + "'," +
                        "    QSXRFKEY       ='" + qsXrfKey + "'," +
                        "    QSKSPASSWD     ='" + qsKsPassword + "'," +
                        "    NOTE           ='" + note + "'," +
                        "    DBSID          ='" + dbSid + "'," +
                        "    DBPORT         ='" + dbPort + "'," +
                        "    QSUSERHEADER   ='" + qsUserHeader + "'," +
                        "    ENVIRONMENT    ='" + environment + "'," +
                        "    CAME           ='" + came + "'," +
                        "    DATALASTMODIFY = SYSDATE " +
                        "WHERE FARMID like '" + farmId + "'");
                isUpdated = resultSet.next();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isUpdated;
    }

    public boolean deleteFarmById(String farmId) {
        connectDBORA();
        boolean isFarmDeleted = false;
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName());
                resultSet = statement.executeQuery("DELETE FROM " + qsFarms + " WHERE FARMID LIKE '" + farmId + "'");
                isFarmDeleted = resultSet.next();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isFarmDeleted;
    }

    public boolean deleteAdminModerById(int id) {
        connectDBORA();
        boolean isAdminModerUserDeleted = false;
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - DELETE FROM " + qsAdminUsers + " WHERE ID LIKE '" + id + "'");
                resultSet = statement.executeQuery("DELETE FROM " + qsAdminUsers + " WHERE ID LIKE '" + id + "'");
                isAdminModerUserDeleted = resultSet.next();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isAdminModerUserDeleted;
    }

    public boolean resetPasswordByUserId(String adminId, String password) {
        boolean isPwdUpdated = false;
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperationCentralized.class.getSimpleName() + " - UPDATE " + qsAdminUsers + " SET PASSWORD = '" + MD5(password) + "' WHERE ID LIKE '" + adminId + "'");
                resultSet = statement.executeQuery("update " + qsAdminUsers + " set PASSWORD = '" + MD5(password) + "' where ID like '" + adminId + "'");
                isPwdUpdated = resultSet.next();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperationCentralized.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isPwdUpdated;
    }
}

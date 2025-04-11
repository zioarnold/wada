package eni.it.gsrestservice.db;

import eni.it.gsrestservice.config.LoggingMisc;
import eni.it.gsrestservice.model.QsAdmins;
import eni.it.gsrestservice.model.QsAudit;
import eni.it.gsrestservice.model.QsFarms;
import oracle.jdbc.driver.OracleDriver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static eni.it.gsrestservice.utility.Utility.MD5;

@SuppressWarnings("SqlResolve")
public class DBOracleOperations {
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

    public DBOracleOperations() {
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
        DBOracleOperations.hostname = hostname;
        DBOracleOperations.port = port;
        DBOracleOperations.sid = sid;
        DBOracleOperations.username = username;
        DBOracleOperations.password = password;
        DBOracleOperations.qsAdminUsers = qsAdminUsers;
        DBOracleOperations.qsFarms = qsFarms;
    }

    public void updateAudit(String query) {
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBOracleOperations.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - " + query);
                resultSet = statement.executeQuery(query);
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBOracleOperations.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBOracleOperations.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
    }

    void connectDBORA() {
        loggingMisc = new LoggingMisc();
        try {
            loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Initializing OracleSQL Driver");
            DriverManager.registerDriver(new OracleDriver());
            loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Initializing OracleSQL Driver Successful");
            loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Initializing connection to DB");
            setConnection(DriverManager.getConnection(hostname + ":" + port + ":" + sid, username, password));
        } catch (SQLException e) {
            e.printStackTrace();
            loggingMisc.printConsole(2, DBOracleOperations.class.getSimpleName() + " - Failed to connect: " + e.getSQLState()
                                        + "Connection is " + getConnection());
        }
    }

    void disconnectDBORA() {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Checking if resultSet is not null");
        if (resultSet != null) {
            try {
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Trying to close resultSet");
                resultSet.close();
                if (resultSet.isClosed()) {
                    loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Closing resultSet successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resultSet = null;
        }
        loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Checking if statement is not null");
        if (statement != null) {
            try {
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Trying to close statement");
                statement.close();
                if (statement.isClosed()) {
                    loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Closing statement successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement = null;
        }
        loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Checking if connection is not null");
        if (connection != null) {
            try {
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Trying to close connection");
                connection.close();
                if (connection.isClosed()) {
                    loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Closing connection successful");
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

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        DBOracleOperations.connection = connection;
    }


    public static boolean isIsAuthenticated() {
        return isAuthenticated;
    }


    public boolean resetPasswordByUserId(String adminId, String password) {
        boolean isPwdUpdated = false;
        connectDBORA();
        try {
            loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBOracleOperations.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBOracleOperations.class.getSimpleName() + " - UPDATE " + qsAdminUsers + " SET PASSWORD = '" + MD5(password) + "' WHERE ID LIKE '" + adminId + "'");
                resultSet = statement.executeQuery("update " + qsAdminUsers + " set PASSWORD = '" + MD5(password) + "' where ID like '" + adminId + "'");
                isPwdUpdated = resultSet.next();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBOracleOperations.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBOracleOperations.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDBORA();
        }
        disconnectDBORA();
        return isPwdUpdated;
    }
}

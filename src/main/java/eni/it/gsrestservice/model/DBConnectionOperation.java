package eni.it.gsrestservice.model;

import eni.it.gsrestservice.config.LoggingMisc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/* ********************************************************
 * DB Connection and Operations
 *
 * Created by Arnold Charyyev - 18/09/2016
 * Edited 19/09/2019
 *
 * Release 1.0.0
 * JDK 1.8.u181++
 * ********************************************************/

@SuppressWarnings("DuplicatedCode")
public class DBConnectionOperation {
    private static Statement statement; //oggetto per effettuare le query
    private static Connection connection; //oggetto che conterra' la stringa di connessione
    private final QlikSenseConnector qlikSenseConnector;
    private final DBConnectionOperationCentralized dbConnectionOperationCentralized;
    private LoggingMisc loggingMisc; //oggetto di log
    private ResultSet resultSet; //oggetto per restituire risultato dallo statement
    private final List<QUsers> qUsersList;
    private List<String> types;
    private List<String> values;

    private static String hostname;
    private static String port;
    private static String sid;
    private static String username;
    private static String password;

    private static String qsUsersTbl;
    private static String qsUsersAttribTbl;

    public static int usersProcessed, usersUploaded;

    private static File roleExist;

    /*
     * costruttore vuoto per inizializzare gli oggetti
     */

    public DBConnectionOperation() {
        loggingMisc = null;
        statement = null;
        resultSet = null;
        qUsersList = new ArrayList<>();
        types = new ArrayList<>();
        values = new ArrayList<>();
        qlikSenseConnector = new QlikSenseConnector();
        dbConnectionOperationCentralized = new DBConnectionOperationCentralized();
    }

    public void initDB(
            String hostname,
            String port,
            String sid,
            String username,
            String password,
            String qsUsersTbl,
            String qsUsersAttribTbl) {
        DBConnectionOperation.hostname = hostname;
        DBConnectionOperation.port = port;
        DBConnectionOperation.sid = sid;
        DBConnectionOperation.username = username;
        DBConnectionOperation.password = password;
        DBConnectionOperation.qsUsersTbl = qsUsersTbl;
        DBConnectionOperation.qsUsersAttribTbl = qsUsersAttribTbl;
    }

    public List<QUsers> getAllUsers() {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        types.clear();
        values.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                resultSet = statement.executeQuery("select " + qsUsersTbl + ".userid, name, user_is_active from " + qsUsersTbl);
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers(resultSet.getString("userid"),
                            resultSet.getString("name"),
                            resultSet.getString("user_is_active")
                    );
                    qUsersList.add(new QUsers(qUsers));
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return qUsersList;
    }

    public List<QUsers> findQUser(String userID) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        types.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - select userid, name, user_is_active from " + qsUsersTbl + " " +
                        "where userid like '" + userID.toUpperCase() + "'");
                resultSet = statement.executeQuery("select userid, name, user_is_active from " + qsUsersTbl + " " +
                        "where userid like '" + userID.toUpperCase() + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers(resultSet.getString("userid"),
                            resultSet.getString("name"),
                            resultSet.getString("user_is_active")
                    );
                    qUsersList.add(qUsers);
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return qUsersList;
    }

    public static void resetCounter() {
        usersUploaded = 0;
        usersProcessed = 0;
    }

    void insertQUserAttribute(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - checking if " + roleExist + " exists");
            FileOutputStream fileOutputStream;
            if (!roleExist.exists()) {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " not exists");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " creating");
                if (roleExist.createNewFile()) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " creating successful");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " opening");
                    fileOutputStream = new FileOutputStream(roleExist, true);
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " opening successful");
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " opening");
                    fileOutputStream = new FileOutputStream(roleExist, true);
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " opening successful");
                }
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " opening");
                fileOutputStream = new FileOutputStream(roleExist, true);
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - " + roleExist + " opening successful");
            }
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if " + type + " is ruolo");
                if (type.equals("ruolo")) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Looking up for roles for " + userID.toUpperCase());
                    resultSet = statement.executeQuery("select count(type) from " + qsUsersAttribTbl + " where userid like '" + userID.toUpperCase() + "' and type like '" + type + "'");
                    resultSet.next();
                    if (resultSet.getInt(1) == 0) {
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - No records found, inserting the attributes for " + userID.toUpperCase());
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - insert into " + qsUsersAttribTbl + " (" + userID.toUpperCase() + ", " + type + ", " + value + ") VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "'");
                        resultSet = statement.executeQuery("insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                        resultSet.next();
                        String roleNotExists = "Ruolo non esiste per" + userID.toUpperCase() + " inserisco";
                        fileOutputStream.write(roleNotExists.getBytes());
                    } else {
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Records found, updating with last data");
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - update " + qsUsersAttribTbl + " set value ='" + value + "' where type like '" + type + "'");
                        resultSet = statement.executeQuery("update " + qsUsersAttribTbl + " set value ='" + value + "' where type like '" + type + "' and userid like '" + userID + "'");
                        resultSet.next();
                        String updatingRole = "Ruolo esiste per " + userID.toUpperCase() + " aggiorno";
                        fileOutputStream.write(updatingRole.getBytes());
                    }
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Looking up for groups for " + userID.toUpperCase());
                    resultSet = statement.executeQuery("select count(type) from " + qsUsersAttribTbl + " where userid like '" + userID.toUpperCase() + "' and type like '" + type + "'");
                    resultSet.next();
                    if (resultSet.getInt(1) == 0) {
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                        resultSet = statement.executeQuery("insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                        resultSet.next();
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                    } else {
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                        resultSet = statement.executeQuery("insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                        resultSet.next();
                        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                    }
                }
            }
            fileOutputStream.close();
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + "  " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        } catch (FileNotFoundException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - File not found " + e.getLocalizedMessage());
        } catch (IOException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Generic error IO " + e.getLocalizedMessage());
        }
    }

    void insertQUserAttributeEmail(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                resultSet = statement.executeQuery("select count(USERID) from " + qsUsersAttribTbl + " where USERID like '" + userID + "' and type like '" + type + "' and value like '" + value + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE) VALUES ('" + userID + "', '" + type + "',$$" + value + "$$);");
                    resultSet = statement.executeQuery("insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - User: " + userID + " exists on table " + qsUsersAttribTbl + ", skipping...");
                }
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + ":" + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    void connectDB() {
        loggingMisc = new LoggingMisc();
        try {
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Initializing PostreSQL Driver");
            DriverManager.registerDriver(new org.postgresql.Driver());
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Initializing PostreSQL Driver Successful");
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Initializing connection to DB");
            setConnection(DriverManager.getConnection(hostname + ":" + port + "/" + sid, username, password));
        } catch (SQLException e) {
            e.printStackTrace();
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Failed to connect: " + e.getSQLState()
                    + "Connection is " + getConnection());
        }
    }

    void disconnectDB() {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if resultSet is not null");
        if (resultSet != null) {
            try {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Trying to close resultSet");
                resultSet.close();
                if (resultSet.isClosed()) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Closing resultSet successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            resultSet = null;
        }
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if statement is not null");
        if (statement != null) {
            try {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Trying to close statement");
                statement.close();
                if (statement.isClosed()) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Closing statement successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            statement = null;
        }
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is not null");
        if (connection != null) {
            try {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Trying to close connection");
                connection.close();
                if (connection.isClosed()) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Closing connection successful");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            connection = null;
        }
        try {
            DriverManager.deregisterDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<QUsers> findUserTypeByUserID(String userId) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - SELECT userid ,type, value FROM " + qsUsersAttribTbl + " WHERE userid like '" + userId.toUpperCase() + "'");
                resultSet = statement.executeQuery("SELECT userid ,type, value FROM " + qsUsersAttribTbl + " WHERE userid like '" + userId.toUpperCase() + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers();
                    qUsers.setUserId(resultSet.getString("userid"));
                    qUsers.setType(resultSet.getString("type"));
                    qUsers.setValue(resultSet.getString("value"));
                    qUsersList.add(qUsers);
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qUsersList;
    }

    public List<QUsers> findUserRoleByUserID(String userId) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - select " + qsUsersTbl + ".userid, name, user_is_active, type, value FROM "
                        + qsUsersTbl + " left outer join " + qsUsersAttribTbl + " qdua on "
                        + qsUsersTbl + ".userid = qdua.userid WHERE " + qsUsersTbl + ".userid like '" + userId.toUpperCase() + "'");
                resultSet = statement.executeQuery("select " + qsUsersTbl + ".userid, name, user_is_active, type, value FROM "
                        + qsUsersTbl + " left outer join " + qsUsersAttribTbl + " qdua on "
                        + qsUsersTbl + ".userid = qdua.userid WHERE " + qsUsersTbl + ".userid like '" + userId.toUpperCase() + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers();
                    qUsers.setUserId(resultSet.getString("userid"));
                    qUsers.setName(resultSet.getString("name"));
                    qUsers.setUserIsActive(resultSet.getString("user_is_active"));
                    qUsers.setType(resultSet.getString("type"));
                    qUsers.setValue(resultSet.getString("value"));
                    if (qlikSenseConnector.getUserRoleByUserId(userId) != null) {
                        qUsers.setNewUserRole(qlikSenseConnector.getUserRoleByUserId(userId));
                    } else {
                        qUsers.setNewUserRole("N/A");
                    }
                    qUsersList.add(qUsers);
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qUsersList;
    }

    void insertQUserAttributeOU(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                resultSet = DBConnectionOperation.statement.executeQuery("select count(USERID) from " + qsUsersAttribTbl + " where USERID like '" + userID + "' and type like '" + type + "' and value like '" + value + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Organization is not detected");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - insert into Q_USERS_ATTRIB (USERID, TYPE, VALUE) VALUES ('" + userID + "', '" + type + "','" + value + "');");
                    resultSet = statement.executeQuery("insert into " + qsUsersAttribTbl + " (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                    disconnectDB();
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Organization is  detected");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing update query");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - update " + qsUsersAttribTbl + " set value ='" + value + "' where type like '" + type + "' and userid like '" + userID + "'");
                    resultSet = DBConnectionOperation.statement.executeQuery("update " + qsUsersAttribTbl + " set value ='" + value + "' where type like '" + type + "' and userid like '" + userID + "'");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - update " + qsUsersAttribTbl + " set value ='" + value + "' where type like '" + type + "' and userid like '" + userID + "' Successful");
                    disconnectDB();
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    private Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        DBConnectionOperation.connection = connection;
    }

    public boolean updateRoleByUserID(String userId, String roleGroup, String oldRole, String userRole) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        try {
            connectDB();
            dbConnectionOperationCentralized.connectDBORA();
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. ");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - update qs_dev_users_attrib set value = '" + userRole + "' where type like 'ruolo' and userid like '" + userId.toUpperCase() + "'");
                if (roleGroup.equalsIgnoreCase("ruolo")) {
                    resultSet = statement.executeQuery("update " + qsUsersAttribTbl + " set value = '" + userRole + "' where value like '" + oldRole + "' and type like 'ruolo' and userid like '" + userId.toUpperCase() + "'");
                } else {
                    resultSet = statement.executeQuery("update " + qsUsersAttribTbl + " set value = '" + userRole + "' where value like '" + oldRole + "' and type like 'gruppo' and userid like '" + userId.toUpperCase() + "'");
                }
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers();
                    qUsers.setUserId(resultSet.getString("userid"));
                    qUsers.setType(resultSet.getString("type"));
                    qUsers.setValue(resultSet.getString("value"));
                    qUsersList.add(qUsers);
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return true;
    }

    public boolean deleteRoleGroupByUserID(String userId, String type, String value) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - delete from " + qsUsersAttribTbl + " where userid like '" + userId.toUpperCase() + "' and type like '" + type + "' and value like '" + value + "'");
                resultSet = statement.executeQuery("delete from " + qsUsersAttribTbl + " where userid like '" + userId.toUpperCase() + "' and type like '" + type + "' and value like '" + value + "'");
                resultSet.next();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return true;
    }

    public boolean deleteUserID(String userId) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            dbConnectionOperationCentralized.connectDBORA();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - delete from " + qsUsersTbl + " where userid like '" + userId.toUpperCase() + "'");
                resultSet = statement.executeQuery("delete from " + qsUsersTbl + " where userid like '" + userId.toUpperCase() + "'");
                resultSet.next();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return true;
    }

    public int getCountUsersOnDB() {
        int usersUploaded = 0;
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - select count(*) as numberRows from " + qsUsersTbl);

                resultSet = statement.executeQuery("select count(*) as numberRows from " + qsUsersTbl);
                if (resultSet.getInt(1) != 0) {
                    usersUploaded = resultSet.getInt("numberrows");
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return usersUploaded;
    }

    /*
     * metodo per censire i dati citati sopra al DB.
     * prima di popolare la TBL effettua una select count
     * per verificare se dato l'userID esiste utenza.
     * Se esiste, al momento skippa.
     * Se non esiste - censisce.
     */
    void insertToQSUsers(String userID, String name, String userIsActive) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                resultSet = DBConnectionOperation.statement.executeQuery("SELECT COUNT(USERID) as exist_usr FROM " + qsUsersTbl + " WHERE USERID LIKE '" + userID.toUpperCase() + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    usersUploaded++;
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                    loggingMisc.printConsole(1, "- INSERT INTO " + qsUsersTbl + " (USERID, name, user_is_active, DATA_LAST_MODIFY) VALUES('" + userID + "','" + name + "','" + userIsActive + "', now())");
                    resultSet = statement.executeQuery("INSERT INTO " + qsUsersTbl + " (USERID, name, user_is_active, DATA_LAST_MODIFY) VALUES('" + userID.toUpperCase() + "','" + name + "','" + userIsActive + "', now())");
                    resultSet.next();
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                } else {
                    usersProcessed++;
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - User: " + userID + " exists on table " + qsUsersAttribTbl + ", skipping...");
                }
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    public boolean insertIntoAttribGroup(String userId, String type, String userGroup) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, " - Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                if (type.equals("gruppo")) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - INSERT INTO " + qsUsersAttribTbl + " (userid, type, value) VALUES('" + userId.toUpperCase() + "','gruppo','" + userGroup + "')");
                    resultSet = statement.executeQuery("INSERT INTO " + qsUsersAttribTbl + " (userid, type, value) VALUES('" + userId.toUpperCase() + "','" + type + "','" + userGroup + "')");
                    resultSet.next();
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                }
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return true;
    }

    public boolean synchronizeUserRole(String userId, String oldRole, String newRole) {
        try {
            connectDB();
            dbConnectionOperationCentralized.connectDBORA();
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - update " + qsUsersAttribTbl + " set value = '" + newRole + "' where value like '" + oldRole + "' and type like 'ruolo' and userid like '" + userId.toUpperCase() + "'");
                resultSet = statement.executeQuery("update " + qsUsersAttribTbl + " set value = '" + newRole + "' where value like '" + oldRole + "' and type like 'ruolo' and userid like '" + userId.toUpperCase() + "'");
                resultSet.next();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
            if (dbConnectionOperationCentralized.getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " Connection is null. Aborting program");
            } else {
                Statement statement = dbConnectionOperationCentralized.getConnection().createStatement();
                ResultSet resultSet = statement.executeQuery("insert into QSAUDITLOG (DESCRIPTION) VALUE ('Utenza " + QsAdminUsers.username + " su qesta farm: " +
                        Farm.description + " di " + Farm.environment + " ha eseguto questa query: update " + qsUsersAttribTbl + " set value = " + newRole + " where value like " + oldRole + " and type like ruolo and userid like " + userId.toUpperCase() + "')");
                resultSet.next();
                dbConnectionOperationCentralized.disconnectDBORA();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
            dbConnectionOperationCentralized.disconnectDBORA();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
            dbConnectionOperationCentralized.disconnectDBORA();
        }
        return true;
    }

    public void initFile(String file) {
        DBConnectionOperation.roleExist = new File(file);
    }

    public boolean disableUserById(String userId, String disableYN) {
        try {
            connectDB();
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Checking if connection is null");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - update " + qsUsersTbl + " set user_is_active = 'N' where userid like '" + userId.toUpperCase() + "'");
                resultSet = statement.executeQuery("update " + qsUsersTbl + " set user_is_active = '" + disableYN + "' where userid like '" + userId.toUpperCase() + "'");
                resultSet.next();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " - Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " - " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return true;
    }
}


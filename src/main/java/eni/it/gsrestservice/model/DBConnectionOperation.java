package eni.it.gsrestservice.model;

import eni.it.gsrestservice.config.LoggingMisc;

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
    private LoggingMisc loggingMisc; //oggetto di log
    private ResultSet resultSet; //oggetto per restituire risultato dallo statement
    private List<QUsers> qUsersList;
    private List<String> types;
    private List<String> values;

    private static String hostname;
    private static String port;
    private static String sid;
    private static String username;
    private static String password;
    private String driver;

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
        driver = "org.postgresql.Driver";
    }

    public List<QUsers> getAllUsers() {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        types.clear();
        values.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query");
                resultSet = statement.executeQuery("select qs_dev_users.userid, name, user_is_active " +
                        "from qs_dev_users ");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers(resultSet.getString("userid"),
                            resultSet.getString("name"),
                            resultSet.getString("user_is_active")
                    );
                    qUsersList.add(new QUsers(qUsers));
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query - successful");
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return qUsersList;
    }

    public List<QUsers> findQUser(String userID) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        types.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "select userid, name, user_is_active from qs_dev_users " +
                        "where userid like '" + userID.toUpperCase() + "'");
                resultSet = statement.executeQuery("select userid, name, user_is_active " +
                        "from qs_dev_users " +
                        "where userid like '" + userID.toUpperCase() + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers(resultSet.getString("userid"),
                            resultSet.getString("name"),
                            resultSet.getString("user_is_active")
                    );
                    qUsersList.add(qUsers);
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query - successful");
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return qUsersList;
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
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Creating statement Successful");
                resultSet = statement.executeQuery("SELECT COUNT(USERID) as exist_usr FROM qs_dev_users WHERE USERID LIKE '" + userID + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, "Executing query");
                    loggingMisc.printConsole(1, "INSERT INTO qs_dev_users (USERID, name, user_is_active, DATA_LAST_MODIFY) +" +
                            "                            VALUES('" + userID + "','" + name + "','" + userIsActive + "', now())");
                    resultSet = statement.executeQuery("INSERT INTO qs_dev_users (USERID, name, user_is_active, DATA_LAST_MODIFY)" +
                            "VALUES('" + userID.toUpperCase() + "','" + name + "','" + userIsActive + "', now())");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query - successful");
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "User: " + userID + " exists on table FARM_QSENSE, skipping...");
                }
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    void insertQUserAttribute(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "insert into qs_users_attrib (USERID, TYPE, VALUE, data_last_modify) VALUES ('\" + userID + \"','\" + type + \"','\" + value + \"', now());");
                resultSet = statement.executeQuery("insert into qs_dev_users_attrib (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query - successful");
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + " " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    void insertQUserAttributeEmail(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Creating statement Successful");
                resultSet = statement.executeQuery("select count(USERID) from qs_dev_users_attrib where USERID like '" + userID + "' and type like '" + type + "' and value like '" + value + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "insert into Q_USERS_ATTRIB (USERID, TYPE, VALUE) VALUES ('" + userID + "', '" + type + "','" + value + "');");
                    resultSet = statement.executeQuery("insert into qs_dev_users_attrib (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query - successful");
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "User: " + userID + " exists on table Q_USERS_ATTRIB, skipping...");
                }
            }
            disconnectDB();
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + " " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    void connectDB() {
        loggingMisc = new LoggingMisc();
        try {
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Initializing PostreSQL Driver");
            Class.forName(driver);
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Initializing PostreSQL Driver Successful");
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Initializing connection to DB");
            setConnection(DriverManager.getConnection(hostname + ":" + port + "/" + sid, username, password));
        } catch (SQLException e) {
            e.printStackTrace();
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Failed to connect: " + e.getSQLState()
                    + "Connection is " + getConnection());
        } catch (ClassNotFoundException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Driver does not exist " + e.getMessage());
        }
    }

    private void disconnectDB() {
        loggingMisc = new LoggingMisc();
        try {
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Closing statement");
            statement.close();
            resultSet.close();
            loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Closing statement Successful");
        } catch (SQLException e) {
            try {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Trying to close connection to DB: " + e.getMessage());
                getConnection().close();
            } catch (SQLException ex) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Unable to close connection to DB: " + ex.getMessage());
            }
        } catch (NullPointerException e1) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Unable to close statement: " + e1.getMessage());
        }
    }

    private Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        DBConnectionOperation.connection = connection;
    }

    public List<QUsers> findUserTypeByUserID(String userId) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Creating statement Successful");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query");
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "SELECT type FROM qs_dev_users_attrib WHERE userid like '" + userId.toUpperCase() + "'");
                resultSet = statement.executeQuery("SELECT type, value FROM qs_dev_users_attrib WHERE userid like '" + userId.toUpperCase() + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers();
                    qUsers.setType(resultSet.getString("type"));
                    qUsers.setValue(resultSet.getString("value"));
                    qUsersList.add(qUsers);
                }
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query - successful");
                disconnectDB();
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return qUsersList;
    }

    void insertQUserAttributeOU(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Checking if connection is null");
        try {
            connectDB();
            if (getConnection() == null) {
                loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Creating statement Successful");
                resultSet = statement.executeQuery("select count(USERID) from qs_dev_users_attrib where USERID like '" + userID + "' and type like '" + type + "' and value like '" + value + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "insert into Q_USERS_ATTRIB (USERID, TYPE, VALUE) VALUES ('" + userID + "', '" + type + "','" + value + "');");
                    resultSet = statement.executeQuery("insert into qs_dev_users_attrib (USERID, TYPE, VALUE, data_last_modify) VALUES ('" + userID.toUpperCase() + "','" + type + "','" + value + "', now())");
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "Executing query - successful");
                    disconnectDB();
                } else {
                    loggingMisc.printConsole(1, DBConnectionOperation.class.getSimpleName() + " " + "User: " + userID + " exists on table Q_USERS_ATTRIB, skipping...");
                    disconnectDB();
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, DBConnectionOperation.class.getSimpleName() + " " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    public void initDB(String hostname, String port, String sid, String username, String password) {
        DBConnectionOperation.hostname = hostname;
        DBConnectionOperation.port = port;
        DBConnectionOperation.sid = sid;
        DBConnectionOperation.username = username;
        DBConnectionOperation.password = password;
    }
}


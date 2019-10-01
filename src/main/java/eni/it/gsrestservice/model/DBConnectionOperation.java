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

/**
 * @class DBConnectionOperation una classe di initizializzazione di connessione/disconessione al DB
 * esecuzione delle varie query
 */
@SuppressWarnings("DuplicatedCode")
public class DBConnectionOperation {
    private static Statement statement; //oggetto per effettuare le query
    private static Connection connection; //oggetto che conterra' la stringa di connessione
    private LoggingMisc loggingMisc; //oggetto di log
    private ResultSet resultSet; //oggetto per restituire risultato dallo statement
    private List<QUsers> qUsersList;
    private List<String> types;
    private List<String> values;

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
    }

    public List<QUsers> getAllUsers() {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        types.clear();
        values.clear();

        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                loggingMisc.printConsole(1, "Executing query");
                resultSet = statement.executeQuery("SELECT Q_USERS.USERID," +
                        " CAME," +
                        " DESCRIZIONE," +
                        " NOTE," +
                        " LIVELLO_SERVIZIO," +
                        " NAME," +
                        " USER_TYPE," +
                        " USER_GROUP," +
                        " USER_IS_ACTIVE," +
                        " ORGANIZZAZIONE," +
                        "EMAIL " +
                        "FROM Q_USERS, FARM_QSENSE, USERS_1 " +
                        "WHERE Q_USERS.USERID = FARM_QSENSE.USERID " +
                        "AND FARM_QSENSE.USERID = USERS_1.USERID ");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers(resultSet.getString("USERID"),
                            resultSet.getString("CAME"),
                            resultSet.getString("DESCRIZIONE"),
                            resultSet.getString("NOTE"),
                            resultSet.getString("LIVELLO_SERVIZIO"),
                            resultSet.getString("NAME"),
//                            resultSet.getString("TYPE"),
//                            resultSet.getString("VALUE"),
                            resultSet.getString("USER_TYPE"),
                            resultSet.getString("USER_GROUP"),
                            resultSet.getString("USER_IS_ACTIVE"),
                            resultSet.getString("ORGANIZZAZIONE"),
                            resultSet.getString("EMAIL"));
                    qUsersList.add(new QUsers(qUsers));
                    types.add(qUsers.getType());
                    values.add(qUsers.getValue());
                }
                loggingMisc.printConsole(1, "Executing query - successful");
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return qUsersList;
    }

    public List<QUsers> findQUser(String userID) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        types.clear();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                loggingMisc.printConsole(1, "Executing query");
                resultSet = statement.executeQuery("SELECT Q_USERS.USERID," +
                        " CAME," +
                        " DESCRIZIONE," +
                        " NOTE," +
                        " LIVELLO_SERVIZIO," +
                        " NAME," +
                        " USER_TYPE," +
                        " USER_GROUP," +
                        " USER_IS_ACTIVE," +
                        " ORGANIZZAZIONE," +
                        "EMAIL " +
                        "FROM Q_USERS, FARM_QSENSE, USERS_1 " +
                        "WHERE Q_USERS.USERID = FARM_QSENSE.USERID " +
                        "AND FARM_QSENSE.USERID = USERS_1.USERID " +
                        "AND Q_USERS.USERID like '" + userID + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers(resultSet.getString("USERID"),
                            resultSet.getString("CAME"),
                            resultSet.getString("DESCRIZIONE"),
                            resultSet.getString("NOTE"),
                            resultSet.getString("LIVELLO_SERVIZIO"),
                            resultSet.getString("NAME"),
//                            resultSet.getString("TYPE"),
//                            resultSet.getString("VALUE"),
                            resultSet.getString("USER_TYPE"),
                            resultSet.getString("USER_GROUP"),
                            resultSet.getString("USER_IS_ACTIVE"),
                            resultSet.getString("ORGANIZZAZIONE"),
                            resultSet.getString("EMAIL"));
                    qUsersList.add(qUsers);
                    types.add(qUsers.getType());
                    values.add(qUsers.getValue());
                }
                loggingMisc.printConsole(1, "Executing query - successful");
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
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

    /**
     * @param userID       matricola
     * @param came         codice came
     * @param description  descrizione
     * @param note         note
     * @param serviceLevel livello di servizio
     */

    /*
     * metodo per censire i dati citati sopra al DB.
     * prima di popolare la TBL effettua una select count
     * per verificare se dato l'userID esiste utenza.
     * Se esiste, al momento skippa.
     * Se non esiste - censisce.
     */
    void insertToFarmQSense(String userID, String came, String description, String note, String serviceLevel) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("SELECT COUNT(USERID) as exist_usr FROM FARM_QSENSE WHERE USERID LIKE '" + userID + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, "Executing query");
                    loggingMisc.printConsole(1, "INSERT INTO WADA.FARM_QSENSE (USERID, CAME, DESCRIZIONE, NOTE, LIVELLO_SERVIZIO, DATA_LAST_MODIFY) " +
                            "VALUES('" + userID + "', " + came + ", '" + description + "', '" + note + "', '" + serviceLevel + "', SYSDATE);");
                    resultSet = statement.executeQuery("INSERT INTO WADA.FARM_QSENSE (USERID, CAME, DESCRIZIONE, NOTE, LIVELLO_SERVIZIO, DATA_LAST_MODIFY)" +
                            "VALUES('" + userID + "', 1, 'FARM LAB01', 'NOTA NA', 'SVILUPPO', SYSDATE)");
                    loggingMisc.printConsole(1, "Executing query - successful");
                } else {
                    loggingMisc.printConsole(1, "User: " + userID + " exists on table FARM_QSENSE, skipping...");
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    /**
     * @param userID userID
     * @param name   nome>matricola>givenName
     */

    /*
     * metodo per censire i dati citati sopra al DB.
     * prima di popolare la TBL effettua una select count
     * per verificare se dato l'userID esiste utenza.
     * Se esiste, al momento skippa.
     * Se non esiste - censisce.
     */
    void insertQUser(String userID,
                     String name) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("select count(USERID) from WADA.Q_USERS where USERID like '" + userID + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, "Executing query");
                    loggingMisc.printConsole(1, "INSERT INTO WADA.Q_USERS (USERID, NAME) VALUES('" + userID + "', '" + name + "');");
                    resultSet = statement.executeQuery("INSERT INTO WADA.Q_USERS (USERID, NAME) VALUES('" + userID + "','" + name + "')");
                    loggingMisc.printConsole(1, "Executing query - successful");
                } else {
                    loggingMisc.printConsole(1, "User: " + userID + " exists on table Q_USERS, skipping...");
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    void insertQUserAttribute(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                loggingMisc.printConsole(1, "Executing query");
                loggingMisc.printConsole(1, "insert into Q_USERS_ATTRIB (USERID, TYPE, VALUE) VALUES ('" + userID + "', '" + type + "','" + value + "');");
                resultSet = statement.executeQuery("insert into Q_USERS_ATTRIB (USERID, TYPE, VALUE) VALUES ('" + userID + "','" + type + "','" + value + "')");
                loggingMisc.printConsole(1, "Executing query - successful");
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    void insertQUserAttributeEmail(String userID, String type, String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("select count(Q_USERS_ATTRIB.USERID) from WADA.Q_USERS_ATTRIB where USERID like '" + userID + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, "Executing query");
                    loggingMisc.printConsole(1, "insert into Q_USERS_ATTRIB (USERID, TYPE, VALUE) VALUES ('" + userID + "', '" + type + "','" + value + "');");
                    resultSet = statement.executeQuery("insert into Q_USERS_ATTRIB (USERID, TYPE, VALUE) VALUES ('" + userID + "','" + type + "','" + value + "')");
                    loggingMisc.printConsole(1, "Executing query - successful");
                } else {
                    loggingMisc.printConsole(1, "User: " + userID + " exists on table Q_USERS_ATTRIB, skipping...");
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    /**
     * @param userID       matricola
     * @param userType     tipo di utenza
     * @param userGroup    gruppo di appartenenza
     * @param userActive   utenza attiva o disattiva
     * @param organization organizzazione tipo OPES/E-2
     * @param email        la mail dell'user ID
     */

    /*
     * metodo per censire i dati citati sopra al DB.
     * prima di popolare la TBL effettua una select count
     * per verificare se dato l'userID esiste utenza.
     * Se esiste, al momento skippa.
     * Se non esiste - censisce.
     */
    void insertUsers(String userID,
                     String userType,
                     String userGroup,
                     String userActive,
                     String organization,
                     String email) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("select count(USERID) FROM WADA.USERS_1 WHERE USERID LIKE '" + userID + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, "Executing query");
                    loggingMisc.printConsole(1, "INSERT INTO WADA.USERS_1 (USERID, USER_TYPE, USER_GROUP, USER_IS_ACTIVE, ORGANIZZAZIONE, EMAIL, DATA_LAST_MODIFY) " +
                            "VALUES('" + userID + "', '" + userType + "', '" + userGroup + "', '" + userActive + "', '" + organization + "', '" + email + "', SYSDATE);");
                    resultSet = statement.executeQuery("INSERT INTO WADA.USERS_1 (USERID, USER_TYPE, USER_GROUP, USER_IS_ACTIVE, ORGANIZZAZIONE, EMAIL, DATA_LAST_MODIFY) " +
                            "VALUES ('" + userID + "','" + userType + "','" + userGroup + "','" + userActive + "','" + organization + "','" + email + "', SYSDATE) ");
                    disconnectDB();
                    loggingMisc.printConsole(1, "Executing query - successful");
                } else {
                    loggingMisc.printConsole(1, "User: " + userID + " exists on table USERS_1, skipping...");
                }
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    public void connectDB(String dbHostname,
                          String dbPort,
                          String dbSid,
                          String dbUsername,
                          String dbPassword) {
        loggingMisc = new LoggingMisc();
        try {
            loggingMisc.printConsole(1, "Initializing Oracle Driver");
            Class.forName("oracle.jdbc.driver.OracleDriver");
            loggingMisc.printConsole(1, "Initializing Oracle Driver Successful");
            loggingMisc.printConsole(1, "Initializing connection to DB");
            setConnection(DriverManager.getConnection("jdbc:oracle:thin:@" + dbHostname +
                    ":" + dbPort + ":" + dbSid, dbUsername, dbPassword));
        } catch (SQLException e) {
            e.printStackTrace();
            loggingMisc.printConsole(2, "Failed to connect: " + e.getSQLState()
                    + "Connection is " + getConnection());
        } catch (ClassNotFoundException e) {
            loggingMisc.printConsole(2, "Driver does not exist " + e.getMessage());
        }
    }

    public void disconnectDB() {
        loggingMisc = new LoggingMisc();
        try {
            loggingMisc.printConsole(1, "Closing statement");
            statement.close();
            loggingMisc.printConsole(1, "Closing statement Successful");
        } catch (SQLException e) {
            try {
                loggingMisc.printConsole(1, "Trying to close connection to DB: " + e.getMessage());
                getConnection().close();
            } catch (SQLException ex) {
                loggingMisc.printConsole(2, "Unable to close connection to DB: " + ex.getMessage());
            }
        } catch (NullPointerException e1) {
            loggingMisc.printConsole(2, "Unable to close statement: " + e1.getMessage());
        }
    }

    private Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        DBConnectionOperation.connection = connection;
    }

    public List<String> findUserTypeByUserID(String showUserType) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                loggingMisc.printConsole(1, "Executing query");
                resultSet = statement.executeQuery("SELECT TYPE FROM Q_USERS_ATTRIB WHERE USERID LIKE '" + showUserType + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers();
                    qUsers.setType(resultSet.getString("TYPE"));
                    qUsersList.add(qUsers);
                    types.add(qUsers.getType());
                }
                loggingMisc.printConsole(1, "Executing query - successful");
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return types;
    }

    public List<String> findUserGroupByUserID(String showUserGroup) {
        loggingMisc = new LoggingMisc();
        qUsersList.clear();
        values.clear();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                loggingMisc.printConsole(1, "Executing query");
                resultSet = statement.executeQuery("SELECT TYPE FROM Q_USERS_ATTRIB WHERE USERID LIKE '" + showUserGroup + "'");
                while (resultSet.next()) {
                    QUsers qUsers = new QUsers();
                    qUsers.setValue(resultSet.getString("TYPE"));
                    qUsersList.add(qUsers);
                    values.add(qUsers.getValue());
                }
                loggingMisc.printConsole(1, "Executing query - successful");
            }
        } catch (SQLSyntaxErrorException ex) {
            loggingMisc.printConsole(2, "Syntax error: " + ex.getLocalizedMessage());
            disconnectDB();
        } catch (SQLException e) {
            loggingMisc.printConsole(2, e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
        return values;
    }
}


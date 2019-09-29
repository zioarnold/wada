package eni.it.gsrestservice.model;

import eni.it.gsrestservice.config.LoggingMisc;

import java.sql.*;
import java.util.Collections;
import java.util.HashMap;

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
public class DBConnectionOperation {
    private static Statement statement; //oggetto per effettuare le query
    private static Connection connection; //oggetto che conterra' la stringa di connessione
    private LoggingMisc loggingMisc; //oggetto di log
    private ResultSet resultSet; //oggetto per restituire risultato dallo statement
    private String userID; //matricola
    private String came; //came
    private String note; //note
    private String serviceLevel; //livello di servizio
    private String description; //descrizione
    private boolean statusStatement = false;


    /*
     * costruttore vuoto per inizializzare gli oggetti
     */
    public DBConnectionOperation() {
        loggingMisc = null;
        statement = null;
        resultSet = null;

        userID = "";
        came = "";
        note = "";
        serviceLevel = "";
        description = "";
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
    void insertToFarmQSense(String userID,
                            String came,
                            String description,
                            String note,
                            String serviceLevel) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
                System.exit(-1);
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("SELECT COUNT(USERID) as exist_usr FROM FARM_QSENSE WHERE USERID LIKE '" + userID + "'");
                resultSet.next();
                if (resultSet.getInt(1) == 0) {
                    loggingMisc.printConsole(1, "Executing query");
                    statusStatement = false;
                    loggingMisc.printConsole(1, "INSERT INTO WADA.FARM_QSENSE (USERID, CAME, DESCRIZIONE, NOTE, LIVELLO_SERVIZIO, DATA_LAST_MODIFY) " +
                            "VALUES('" + userID + "', " + came + ", '" + description + "', '" + note + "', '" + serviceLevel + "', SYSDATE);");
                    statusStatement = true;
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
                System.exit(-1);
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

    void insertQUserAttribute(String userID,
                              String type,
                              String value) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
                System.exit(-1);
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("select count(USERID) from WADA.Q_USERS_ATTRIB where USERID like '" + userID + "'");
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
                System.exit(-1);
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
//                resultSet = statement.executeQuery();
//                getConnection().commit();
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

    public void searchUserOnDB(String userID) {
        loggingMisc = new LoggingMisc();
        loggingMisc.printConsole(1, "Checking if connection is null");
        try {
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
                System.exit(-1);
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("SELECT * FROM FARM_QSENSE WHERE USERID LIKE" + "'" + userID + "'");
            }
        } catch (SQLException e) {
            loggingMisc.printConsole(2, "Unknown error " + e.getSQLState() + " " + e.getLocalizedMessage());
            disconnectDB();
        }
    }

    void getAllUsersFromDB() {
        loggingMisc = new LoggingMisc();
        HashMap<String, String> hashMap = new HashMap<>();
        try {
            loggingMisc.printConsole(1, "Checking if connection is null.");
            if (getConnection() == null) {
                loggingMisc.printConsole(2, "Connection is null. Aborting program");
                System.exit(-1);
            } else {
                loggingMisc.printConsole(1, "Connection is not null. Creating statement");
                statement = getConnection().createStatement();
                loggingMisc.printConsole(1, "Creating statement Successful");
                resultSet = statement.executeQuery("SELECT * FROM FARM_QSENSE");
                while (resultSet.next()) {
                    loggingMisc.printConsole(1, "USERID: " + resultSet.getString(2)
                            + " ~ CAME: " + resultSet.getInt(3)
                            + " ~ DESCRIPTION: " + resultSet.getString(4)
                            + " ~ NOTE: " + resultSet.getString(5)
                            + " ~ SERVICE LEVEL: " + resultSet.getString(6));
//                    listDB.add(resultSet.getString(2)); //USERID

                    setUserID(resultSet.getString(2));
                    setCame(resultSet.getString(3)); //CAME
                    setDescription(resultSet.getString(4)); //DESCRIPTION
                    setNote(resultSet.getString(5)); //NOTE
                    setServiceLevel(resultSet.getString(6)); //SERVICE LEVEL
                    hashMap.put(getUserID(), getCame() + ";" + getDescription() + ";" + getNote() + ";" + getServiceLevel());
                }
                if (hashMap.isEmpty()) {
                    loggingMisc.printConsole(2, "Error list is empty");
                } else {
                    loggingMisc.printConsole(1, "Data in HashMap: " + Collections.singletonList(hashMap));
                }
            }
        } catch (SQLException e) {
            disconnectDB();
            loggingMisc.printConsole(2, "Unknown error " + e.getSQLState() + " " + e.getLocalizedMessage());
            System.exit(-1);
        }
    }

    void connectDB(String dbHostname,
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
            System.exit(-1);
        }
    }

    private void disconnectDB() {
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

    private String getUserID() {
        return userID;
    }

    private void setUserID(String userID) {
        this.userID = userID;
    }

    private String getCame() {
        return came;
    }

    private void setCame(String came) {
        this.came = came;
    }

    private String getNote() {
        return note;
    }

    private void setNote(String note) {
        this.note = note;
    }

    private String getServiceLevel() {
        return serviceLevel;
    }

    private void setServiceLevel(String serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    private String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private Connection getConnection() {
        return connection;
    }

    private void setConnection(Connection connection) {
        DBConnectionOperation.connection = connection;
    }

    public boolean isStatusStatement() {
        return statusStatement;
    }

    public void setStatusStatement(boolean statusStatement) {
        this.statusStatement = statusStatement;
    }
}


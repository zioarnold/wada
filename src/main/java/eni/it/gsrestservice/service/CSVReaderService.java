package eni.it.gsrestservice.service;

import eni.it.gsrestservice.config.LoggingMisc;
import eni.it.gsrestservice.model.CSVReader;
import eni.it.gsrestservice.model.LDAPConnector;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@SuppressWarnings("DuplicatedCode")
@Service
public class CSVReaderService {
    private final CSVReader csvReader = new CSVReader();
    private final LDAPConnector ldapConnector = new LDAPConnector();
    private static File fileUsersNotExists;
    private static File userDiscardedByRole;
    public static int rowNumbers;
    public static int userRoleDiscarded;
    private final LoggingMisc loggingMisc;
    private List<String> rolesList;

    public CSVReaderService() {
        loggingMisc = new LoggingMisc();
    }

    public void read(byte[] data) {
        csvReader.setContent(new String(data));
    }

    public boolean readDataCheckLdapInsertIntoDB(byte[] data) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
        try {
            bufferedReader.readLine();
            csvReader.setContent(new String(data));
            rowNumbers = csvReader.count(data);
            String firstRow;
            String[] userID;
            loggingMisc.printConsole(1, CSVReaderService.class.getSimpleName() + " - checking if " + fileUsersNotExists + " exists");
            FileOutputStream fileOutputStream, outputStream;
            if (!fileUsersNotExists.exists() || !userDiscardedByRole.exists()) {
                loggingMisc.printConsole(1, CSVReaderService.class.getSimpleName() + " - " + fileUsersNotExists + " not exists, creating");
                if (fileUsersNotExists.createNewFile() || userDiscardedByRole.createNewFile()) {
                    loggingMisc.printConsole(1, CSVReaderService.class.getSimpleName() + " - " + fileUsersNotExists + " created successfully");
                }
            }
            loggingMisc.printConsole(1, CSVReaderService.class.getSimpleName() + " - " + fileUsersNotExists + " opening");
            fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            outputStream = new FileOutputStream(userDiscardedByRole, true);
            loggingMisc.printConsole(1, CSVReaderService.class.getSimpleName() + " - " + fileUsersNotExists + " opening successful");
            while ((firstRow = bufferedReader.readLine()) != null) {
                userID = firstRow.split(";");
                for (String role : getRolesList()) {
                    if (!userID[1].equals(role)) {
                        String userNotExist = "Utente: " + userID[0] + " scartato causa: " + userID[1] + " e` diverso da: " + role + "\n";
                        outputStream.write(userNotExist.getBytes());
                    } else {
                        csvReader.setUserID(userID[0]);//Matricola
                        csvReader.setRole(userID[1]);//Ruolo
                        csvReader.setGroup(userID[2]);//Gruppo
                        ldapConnector.searchOnLDAPInsertToDB(userID[0], userID[1], userID[2]);
                    }
                }
            }
            loggingMisc.printConsole(1, CSVReaderService.class.getSimpleName() + " - " + fileUsersNotExists + " closing");
            fileOutputStream.close();
            outputStream.close();
            loggingMisc.printConsole(1, CSVReaderService.class.getSimpleName() + " - " + fileUsersNotExists + " closing successful");
            return true;
        } catch (Exception e) {
            loggingMisc.printConsole(2, CSVReaderService.class.getSimpleName() + " - Generic error " + e.getLocalizedMessage());
            return false;
        }
    }

    public static void resetCounter() {
        CSVReaderService.userRoleDiscarded = 0;
    }

    public void initFile(String fileUsersNotExists, String userDiscardedByRole) {
        CSVReaderService.fileUsersNotExists = new File(fileUsersNotExists);
        CSVReaderService.userDiscardedByRole = new File(userDiscardedByRole);
    }

    private List<String> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<String> rolesList) {
        this.rolesList = rolesList;
    }
}

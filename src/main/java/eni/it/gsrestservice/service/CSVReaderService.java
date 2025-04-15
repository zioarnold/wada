package eni.it.gsrestservice.service;

import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.model.CSVReader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class CSVReaderService {

    private final CSVReader csvReader = new CSVReader();
    private final RolesListConfig rolesListConfig;
    private final LDAPService ldapService;
    @Value("${log.user.role.discarded}")
    private File userDiscardedByRole;

    private int rowNumbers;
    private int userRoleDiscarded;
    private int usersUploaded;

    /**
     * Reads CSV content and sets it to the reader
     *
     * @param data byte array containing CSV data
     * @throws IllegalArgumentException if data is null or empty
     */
    public void read(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }
        csvReader.setContent(new String(data));
    }

    /**
     * Processes CSV data, validates against LDAP and inserts into database
     *
     * @param data byte array containing CSV data
     * @return true if processing was successful
     */
    public boolean readDataCheckLdapInsertIntoDB(byte[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data cannot be null or empty");
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
             FileOutputStream outputStream = new FileOutputStream(userDiscardedByRole, true)) {

            initializeFiles();
            bufferedReader.readLine(); // skip header
            csvReader.setContent(new String(data));
            rowNumbers = csvReader.count(data);

            processCSVContent(bufferedReader, outputStream);

            return true;
        } catch (IOException e) {
            log.error("Error processing CSV file: ", e);
            return false;
        }
    }

    private void initializeFiles() throws IOException {
        if (!userDiscardedByRole.exists() && !userDiscardedByRole.createNewFile()) {
            throw new IOException("Unable to create file: " + userDiscardedByRole.getName());
        }
    }

    private void processCSVContent(BufferedReader reader, FileOutputStream outputStream) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] userId = line.split(";");
            processUserRecord(userId, outputStream);
        }
    }

    private void processUserRecord(String[] userId, FileOutputStream outputStream) throws IOException {
        for (String role : rolesListConfig.initRolesList()) {
            if (!userId[1].equals(role)) {
                String userNotExist = "Utente: " + userId[0] + " scartato causa: " + userId[1] + " e` diverso da: " + role + "\n";
                userRoleDiscarded++;
                outputStream.write(userNotExist.getBytes());
            } else {
                ldapService.searchOnLDAPInsertToDB(userId[0], userId[1], userId[2]);
                usersUploaded++;
            }
        }
    }

    /**
     * Resets all counters to zero
     */
    public void resetCounter() {
        userRoleDiscarded = 0;
        usersUploaded = 0;
    }
}

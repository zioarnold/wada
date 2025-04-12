package eni.it.gsrestservice.service;

import eni.it.gsrestservice.config.RolesListConfig;
import eni.it.gsrestservice.model.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class CSVReaderService {

    private final CSVReader csvReader = new CSVReader();
    private final RolesListConfig rolesListConfig;
    private final LDAPService ldapService;
    @Value("${log.discard}")
    private static File fileUsersNotExists;
    @Value("${log.user.role.discarded}")
    private static File userDiscardedByRole;
    public static int rowNumbers, userRoleDiscarded, usersUploaded;

    public CSVReaderService(RolesListConfig rolesListConfig, LDAPService ldapService) {
        this.rolesListConfig = rolesListConfig;
        this.ldapService = ldapService;
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
            String[] userId;
            FileOutputStream fileOutputStream, outputStream;
            if (!fileUsersNotExists.exists() || !userDiscardedByRole.exists()) {
                if (!fileUsersNotExists.createNewFile() || userDiscardedByRole.createNewFile()) {
                    log.error("Unable to create files{} and {} ", fileUsersNotExists.getName(), userDiscardedByRole.getName());
                }
            }
            fileOutputStream = new FileOutputStream(fileUsersNotExists, true);
            outputStream = new FileOutputStream(userDiscardedByRole, true);
            while ((firstRow = bufferedReader.readLine()) != null) {
                userId = firstRow.split(";");
                for (String role : rolesListConfig.getList()) {
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
            fileOutputStream.close();
            outputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void resetCounter() {
        userRoleDiscarded = 0;
        usersUploaded = 0;
    }
}

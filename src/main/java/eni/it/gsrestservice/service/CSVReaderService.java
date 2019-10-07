package eni.it.gsrestservice.service;

import eni.it.gsrestservice.model.CSVReader;
import eni.it.gsrestservice.model.LDAPConnector;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVReaderService {
    private CSVReader csvReader = new CSVReader();
    private List<CSVReader> list = new ArrayList<>();
    private LDAPConnector ldapConnector = new LDAPConnector();
    private boolean status;

    public void read(byte[] data) {
        csvReader.setContent(new String(data));
    }

    public boolean readDataCheckLdapInsertIntoDB(byte[] data) throws IOException {
        status = false;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
        bufferedReader.readLine();
        csvReader.setContent(new String(data));
        String firstRow;
        String[] userID;
        while ((firstRow = bufferedReader.readLine()) != null) {
            userID = firstRow.split(";");
            csvReader.setUserID(userID[0]);//Matricola
            csvReader.setRole(userID[1]);//Ruolo
            csvReader.setGroup(userID[2]);//Gruppo
            ldapConnector.searchOnLDAPInsertToDB(userID[0], userID[1], userID[2]);
        }
        status = true;
        return status;
    }
}

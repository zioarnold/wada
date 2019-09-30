package eni.it.gsrestservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eni.it.gsrestservice.model.CSVReader;
import eni.it.gsrestservice.model.LDAPConnector;
import eni.it.gsrestservice.model.LDAPUser;
import eni.it.gsrestservice.parsers.CSV;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CSVReaderService {
    private CSVReader csvReader = new CSVReader();
    private List<CSVReader> list = new ArrayList<>();
    private LDAPConnector ldapConnector = new LDAPConnector();
    private boolean status;

    public String convertToJSON(byte[] file) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(file);
        CSV csv = new CSV(true, ';', new BufferedReader(new InputStreamReader(inputStream)));
        List<String> fieldNames = null;
        if (csv.hasNext()) fieldNames = new ArrayList<>(csv.next());
        List<Map<String, String>> list = new ArrayList<>();
        while (csv.hasNext()) {
            List<String> x = csv.next();
            Map<String, String> obj = new LinkedHashMap<>();
            for (int i = 0; i < fieldNames.size(); i++) {
                obj.put(fieldNames.get(i), x.get(i));
            }
            list.add(obj);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(list);
        return json;
    }

    public void read(byte[] data) {
        csvReader.setContent(new String(data));
    }

    public boolean readDataCheckLdapInsertIntoDB(byte[] data) throws IOException {
        status = false;
        ldapConnector.connectToLDAP("ldap://vds-test.services.eni.intranet:18389/",
                "cn=adminLDAP-QLIK,ou=guest,o=ea-tree", "B3%jUNwb", "dc=pri");
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

    public List<LDAPUser> getUsersNotUploaded() {
        return ldapConnector.getUserNotExistsOnLdap();
    }

    public List<LDAPUser> getUsersUploaded() {
        return ldapConnector.getUserExistsOnLdap();
    }
}

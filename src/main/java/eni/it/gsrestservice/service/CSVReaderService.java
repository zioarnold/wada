package eni.it.gsrestservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eni.it.gsrestservice.model.CSVReader;
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

    public void readData(byte[] data) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));
        bufferedReader.readLine();
        csvReader.setContent(new String(data));
        String firstRow;
        String[] userID;
        while ((firstRow = bufferedReader.readLine()) != null) {
            userID = firstRow.split(";");
            csvReader.setuAccess(userID[0]);
            csvReader.setuNtName(userID[1]);
            csvReader.setuUtente(userID[2]);
//            System.out.println("userID = " + userID[3]);
//            if (userID[3].equals("")) {
//                csvReader.setuGruppo("");
//            } else {
//                csvReader.setuGruppo(userID[3]);
//            }
//            System.out.println("userID = " + userID[4]);
//            if (userID[4].equals("")) {
//                csvReader.setuNote("");
//            } else {
//                csvReader.setuNote(userID[4]);
//            }
            list.add(csvReader);
        }
    }

    public void cleanUp() {
        list.clear();
    }

    public List<CSVReader> getAll() {
        System.out.println("list = " + list);
        return this.list;
    }
}

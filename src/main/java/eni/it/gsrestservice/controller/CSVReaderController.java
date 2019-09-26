package eni.it.gsrestservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import eni.it.gsrestservice.model.CSVReader;
import eni.it.gsrestservice.model.DBConnectionOperation;
import eni.it.gsrestservice.parsers.CSV;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CSVReaderController {
    private CSVReader csvReader = new CSVReader();
    private String csv = "";
    private DBConnectionOperation connectionOperation;

    @RequestMapping(value = "/upload")
    public ModelAndView uploadFile() {
        ModelAndView modelAndView = new ModelAndView("upload");
        return modelAndView;
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            csvReader.setContent(new String(file.getBytes()));
            return new ModelAndView("uploadSuccess");
        }
        return new ModelAndView("error");
    }

    @RequestMapping(value = "/showContent", produces = MediaType.APPLICATION_JSON_VALUE)
    public String showContent() throws IOException {
        InputStream in = new ByteArrayInputStream(csvReader.getContent().getBytes());
        CSV csv = new CSV(true, ';', new BufferedReader(new InputStreamReader(in)));
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
        return mapper.writeValueAsString(list);
    }

    @RequestMapping(value = "/loadToDB", method = RequestMethod.POST)
    public ModelAndView loadToDB(@RequestParam("redirect_url") String redirect_url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csvReader.getContent().getBytes())));
        reader.readLine();
        String firstRow;
        String[] userID = new String[5];
        while ((firstRow = reader.readLine()) != null) {
            userID = firstRow.split(";");
        }
        if (!connectionOperation.isStatusStatement()) {
            connectionOperation.insertToFarmQSense(userID[1], "1", "FARM LAB01", "NOTA NA", "SVILUPPO");
        }
        return new ModelAndView("redirect:" + redirect_url);
    }
}

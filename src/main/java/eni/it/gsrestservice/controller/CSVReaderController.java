package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.DBConnectionOperation;
import eni.it.gsrestservice.service.CSVReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class CSVReaderController {
    @Autowired
    private CSVReaderService csvReaderService;
    @Autowired
    private Environment environment;
    private DBConnectionOperation dbConnectionOperation = new DBConnectionOperation();

    @GetMapping(value = "/singleUpload")
    public ModelAndView uploadFile(HttpServletRequest request) {
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        return new ModelAndView("singleUpload");
    }

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage(HttpServletRequest request) {
        request.setAttribute("farm_name", environment.getProperty("farm.name"));
        request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
        return new ModelAndView("massiveUpload");
    }

    @RequestMapping(value = "/massiveUpload", method = RequestMethod.POST)
    public ModelAndView uploadFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        initDB();
        if (!file.isEmpty()) {
            if (csvReaderService.readDataCheckLdapInsertIntoDB(file.getBytes())) {
                request.setAttribute("farm_name", environment.getProperty("farm.name"));
                request.setAttribute("farm_environment", environment.getProperty("farm.environment"));
                return new ModelAndView("uploadSuccess");
            } else {
                return new ModelAndView("error");
            }
        }
        return new ModelAndView("error");
    }

    private void initDB() {
        dbConnectionOperation.initDB(environment.getProperty("db.hostname"),
                environment.getProperty("db.port"),
                environment.getProperty("db.sid"),
                environment.getProperty("db.username"),
                environment.getProperty("db.password")
        );
    }
}

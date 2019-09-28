package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.model.DBConnectionOperation;
import eni.it.gsrestservice.service.CSVReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class CSVReaderController {
    @Autowired
    private CSVReaderService csvReaderService;
    private DBConnectionOperation connectionOperation;

    @GetMapping(value = "/singleUpload")
    public ModelAndView uploadFile() {
        return new ModelAndView("singleUpload");
    }

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage() {
        return new ModelAndView("massiveUpload");
    }

    @RequestMapping(value = "/massiveUpload", method = RequestMethod.POST)
    public ModelAndView uploadFile(HttpServletRequest request,
                                   @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            csvReaderService.readData(file.getBytes());
            request.setAttribute("filecontent", csvReaderService.getAll());
            csvReaderService.cleanUp();
            return new ModelAndView("massiveUpload");
        }
        return new ModelAndView("error");
    }

//    @RequestMapping(value = "/loadToDB", method = RequestMethod.POST)
//    public ModelAndView loadToDB(@RequestParam("redirect_url") String redirect_url) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(csvReader.getContent().getBytes())));
//        reader.readLine();
//        String firstRow;
//        String[] userID = new String[5];
//        while ((firstRow = reader.readLine()) != null) {
//            userID = firstRow.split(";");
//        }
//        if (!connectionOperation.isStatusStatement()) {
//            connectionOperation.insertToFarmQSense(userID[1], "1", "FARM LAB01", "NOTA NA", "SVILUPPO");
//        }
//        return new ModelAndView("redirect:" + redirect_url);
//    }
//
//    public void setConnectionOperation(DBConnectionOperation connectionOperation) {
//        this.connectionOperation = connectionOperation;
//    }
}

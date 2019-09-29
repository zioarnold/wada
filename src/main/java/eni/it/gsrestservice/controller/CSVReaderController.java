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
            if (csvReaderService.readDataCheckLdapInsertIntoDB(file.getBytes())) {
                return new ModelAndView("uploadSuccess");
            } else {
                return new ModelAndView("error");
            }
//            request.setAttribute("users_not_exist", csvReaderService.getAll());
//            request.setAttribute("users_exist", csvReaderService.getAll());
        }
        return new ModelAndView("error");
    }
}

package eni.it.gsrestservice.controller;

import eni.it.gsrestservice.service.CSVReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@RestController
@PropertySource(value = {"classpath:config.properties", "classpath:application.properties"})
public class CSVReaderController {
    @Autowired
    private CSVReaderService csvReaderService;
    @Autowired
    private Environment environment;

    @GetMapping(value = "/singleUpload")
    public ModelAndView uploadFile() {
        return new ModelAndView("singleUpload");
    }

    @GetMapping(value = "/massiveUploadPage")
    public ModelAndView massiveUploadPage() {
        return new ModelAndView("massiveUpload");
    }

    @RequestMapping(value = "/massiveUpload", method = RequestMethod.POST)
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            if (csvReaderService.readDataCheckLdapInsertIntoDB(file.getBytes(),
                    environment.getProperty("vds.ldapURL"),
                    environment.getProperty("vds.userName"),
                    environment.getProperty("vds.password"),
                    environment.getProperty("vds.baseDN"),
                    environment.getProperty("db.hostname"),
                    environment.getProperty("db.port"),
                    environment.getProperty("db.sid"),
                    environment.getProperty("db.username"),
                    environment.getProperty("db.password"))) {
                return new ModelAndView("uploadSuccess");
            } else {
                return new ModelAndView("error");
            }
        }
        return new ModelAndView("error");
    }

    @GetMapping(value = "/showResults")
    public ModelAndView showUsersExists(HttpServletRequest request,
                                        @RequestParam(required = false, name = "users_not_exist") String usersNotUploaded,
                                        @RequestParam(required = false, name = "users_exist") String usersUploaded) {
        if (usersNotUploaded != null) {
            request.setAttribute("users_not_exist", csvReaderService.getUsersNotUploaded());
            return new ModelAndView("uploadSuccess");
        } else if (usersUploaded != null) {
            request.setAttribute("users_exist", csvReaderService.getUsersUploaded());
            return new ModelAndView("uploadSuccess");
        } else {
            return new ModelAndView("uploadSuccess");
        }
    }
}

package eni.it.gsrestservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ErrorController {
    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public ModelAndView renderErrorPage(HttpServletRequest request) {
        ModelAndView errorPage = new ModelAndView("error");
        String errorMessage = "";
        int errorCode = getErrorCode(request);
        switch (errorCode) {
            case 400: {
                errorMessage = "Http Error Code: 400. Bad Request";
                break;
            }
            case 401: {
                errorMessage = "Http Error Code: 401. Unauthorized";
                break;
            }
            case 404: {
                errorMessage = "Http Error Code: 404. Resource not found";
                break;
            }
            case 500: {
                errorMessage = "Http Error Code: 500. Internal Server Error";
                break;
            }
        }
        errorPage.addObject("errorMsg", errorMessage);
        return errorPage;
    }

    private int getErrorCode(HttpServletRequest httpRequest) {
        return (Integer) httpRequest
                .getAttribute("javax.servlet.error.status_code");
    }
}

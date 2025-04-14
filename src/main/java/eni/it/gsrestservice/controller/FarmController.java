package eni.it.gsrestservice.controller;


import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.entities.postgres.QsFarm;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.post.QsFarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * @author Zio Arnold aka Arni
 * @created 14/04/2025 - 13:26 </br>
 */
@RestController
@RequiredArgsConstructor
public class FarmController {
    private final QsFarmService qsFarmService;
    private final QlikSenseService qlikSenseService;

    @RequestMapping("/createFarm")
    public ModelAndView createFarm() {
        return new ModelAndView("createFarm")
                .addObject("user_logged_in", QsAdminUsers.username)
                .addObject("user_role_logged_in", QsAdminUsers.role);
    }

    @RequestMapping("/selectFarm")
    public ModelAndView selectFarm(@RequestParam(name = "farm") String farmName) {

        Optional<QsFarm> qsFarm = qsFarmService.findByDescription(farmName);
        if (qsFarm.isPresent()) {
            if (qsFarmService.initConnector(qsFarm.get())) {
                return new ModelAndView("index")
                        .addObject("farm_name", Farm.description)
                        .addObject("farm_environment", Farm.environment)
                        .addObject("ping_qlik", qlikSenseService.ping())
                        .addObject("user_logged_in", QsAdminUsers.username)
                        .addObject("user_role_logged_in", QsAdminUsers.role);
            } else {
                return new ModelAndView("errorLogin")
                        .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg());
            }
        } else {
            return new ModelAndView("errorLogin")
                    .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg());
        }
    }
}

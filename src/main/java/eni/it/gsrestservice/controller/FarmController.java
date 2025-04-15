package eni.it.gsrestservice.controller;


import eni.it.gsrestservice.config.ErrorWadaManagement;
import eni.it.gsrestservice.entities.postgres.QsFarm;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.model.QsAdminUsers;
import eni.it.gsrestservice.service.QlikSenseService;
import eni.it.gsrestservice.service.post.QsFarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Zio Arnold aka Arni
 * @created 14/04/2025 - 13:26 </br>
 */
@RestController
@RequiredArgsConstructor
public class FarmController {
    private final QsFarmService qsFarmService;
    private final QlikSenseService qlikSenseService;

    /**
     * Displays the farm creation page
     *
     * @return ModelAndView for farm creation
     */
    @GetMapping("/createFarm")
    public ModelAndView createFarm() {
        ModelAndView modelAndView = new ModelAndView("createFarm");
        modelAndView.addObject("user_logged_in", QsAdminUsers.username);
        modelAndView.addObject("user_role_logged_in", QsAdminUsers.role);
        return modelAndView;
    }

    /**
     * Selects and initializes a farm
     *
     * @param farmName name of the farm to select
     * @return ModelAndView with farm details or error page
     */
    @GetMapping("/selectFarm")
    public ModelAndView selectFarm(@RequestParam(name = "farm") String farmName) {
        return qsFarmService.findByDescription(farmName)
                .map(this::initializeFarm)
                .orElse(createErrorView());
    }

    private ModelAndView initializeFarm(QsFarm farm) {
        if (!qsFarmService.initConnector(farm)) {
            return createErrorView();
        }
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("farm_name", Farm.description);
        modelAndView.addObject("farm_environment", Farm.environment);
        modelAndView.addObject("ping_qlik", qlikSenseService.ping());
        modelAndView.addObject("user_logged_in", QsAdminUsers.username);
        modelAndView.addObject("user_role_logged_in", QsAdminUsers.role);
        return modelAndView;
    }

    private ModelAndView createErrorView() {
        return new ModelAndView("errorLogin")
                .addObject("errorMsg", ErrorWadaManagement.E_0012_FARM_SELECT_PROBLEM.getErrorMsg());
    }
}

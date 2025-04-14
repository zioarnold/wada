package eni.it.gsrestservice.config;


import eni.it.gsrestservice.entities.postgres.QsFarm;
import eni.it.gsrestservice.service.QlikSenseService;
import org.springframework.stereotype.Component;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 16:38 </br>
 */
@Component
public class QlikViewSenseConfig {
    public void configureFarm(QsFarm farm) {
        QlikSenseService.initConnector(
                farm.getQsXrfKey(),
                farm.getQsHost(),
                farm.getQsPathClient(),
                farm.getQsPathRoot(),
                farm.getQsKsPasswd(),
                farm.getQsUserHeader(),
                farm.getQsReloadTaskName()
        );
        QlikSenseService.configureCertificate();
    }
}

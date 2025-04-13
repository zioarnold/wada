package eni.it.gsrestservice.service.ora;


import eni.it.gsrestservice.config.QlikViewSenseConfig;
import eni.it.gsrestservice.entities.oracle.QsFarm;
import eni.it.gsrestservice.model.Farm;
import eni.it.gsrestservice.repos.ora.QsFarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:48 </br>
 */
@Service
@RequiredArgsConstructor
public class QsFarmService {
    private final QlikViewSenseConfig qlikViewSenseConfig;
    private final QsFarmRepository qsFarmRepository;

    public List<QsFarm> findAllFarms() {
        return qsFarmRepository.findAll();
    }

    public Optional<QsFarm> findById(String id) {
        return qsFarmRepository.findByFarmId(id);
    }

    public QsFarm create(QsFarm qsFarm) {
        return qsFarmRepository.save(qsFarm);
    }

    public void delete(QsFarm qsFarm) {
        qsFarmRepository.delete(qsFarm);
    }

    public void deleteById(String id) {
        qsFarmRepository.deleteById(id);
    }

    public QsFarm update(QsFarm qsFarm) {
        return qsFarmRepository.save(qsFarm);
    }

    public Optional<QsFarm> findByDescription(String farmName) {
        return qsFarmRepository.findByDescription(farmName);
    }

    public boolean initConnector(QsFarm qsFarm) {
        qlikViewSenseConfig.configureFarm(qsFarm);
        Farm.farmId = qsFarm.getFarmId();
        Farm.description = qsFarm.getDescription();
        Farm.came = qsFarm.getCame();
        Farm.dbUser = qsFarm.getDbUser();
        Farm.dbPassword = qsFarm.getDbPassword();
        Farm.dbHost = qsFarm.getDbHost();
        Farm.dbPort = qsFarm.getDbPort();
        Farm.dbSid = qsFarm.getDbSid();
        Farm.qsHost = qsFarm.getQsHost();
        Farm.qsHeader = qsFarm.getQsUserHeader();
        Farm.qsPathClientJKS = qsFarm.getQsPathClient();
        Farm.qsPathRootJKS = qsFarm.getQsPathRoot();
        Farm.qsKeyStorePwd = qsFarm.getQsKsPasswd();
        Farm.qsXrfKey = qsFarm.getQsXrfKey();
        Farm.note = qsFarm.getNote();
        Farm.environment = qsFarm.getEnvironment();
        Farm.qsReloadTaskName = qsFarm.getQsReloadTaskName();
        return true;
    }
}

package eni.it.gsrestservice.service.ora;


import eni.it.gsrestservice.config.QlikViewSenseConfig;
import eni.it.gsrestservice.db.repos.ora.QsFarmRepository;
import eni.it.gsrestservice.entities.oracle.QsFarm;
import eni.it.gsrestservice.model.Farm;
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
        return qsFarmRepository.findByFarmid(id);
    }

    public QsFarm create(QsFarm qsFarm) {
        return qsFarmRepository.save(qsFarm);
    }

    public void delete(QsFarm qsFarm) {
        qsFarmRepository.delete(qsFarm);
    }

    public void deleteById(Long id) {
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

        Farm.farmId = qsFarm.getFarmid();
        Farm.description = qsFarm.getDescription();
        Farm.came = qsFarm.getCame();
        Farm.dbUser = qsFarm.getDbuser();
        Farm.dbPassword = qsFarm.getDbpassword();
        Farm.dbHost = qsFarm.getDbhost();
        Farm.dbPort = qsFarm.getDbport();
        Farm.dbSid = qsFarm.getDbsid();
        Farm.qsHost = qsFarm.getQshost();
        Farm.qsHeader = qsFarm.getQsuserheader();
        Farm.qsPathClientJKS = qsFarm.getQspathclient();
        Farm.qsPathRootJKS = qsFarm.getQspathroot();
        Farm.qsKeyStorePwd = qsFarm.getQskspasswd();
        Farm.qsXrfKey = qsFarm.getQsxrfkey();
        Farm.note = qsFarm.getNote();
        Farm.environment = qsFarm.getEnvironment();
        Farm.qsReloadTaskName = qsFarm.getQsreloadtaskname();
        return true;
    }
}

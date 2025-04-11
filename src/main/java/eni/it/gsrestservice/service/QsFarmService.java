package eni.it.gsrestservice.service;


import eni.it.gsrestservice.db.repos.ora.QsFarmRepository;
import eni.it.gsrestservice.entities.oracle.QsFarm;
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

    private final QsFarmRepository qsFarmRepository;

    public List<QsFarm> findAllFarms() {
        return qsFarmRepository.findAll();
    }

    public Optional<QsFarm> findById(Long id) {
        return qsFarmRepository.findById(id);
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
}

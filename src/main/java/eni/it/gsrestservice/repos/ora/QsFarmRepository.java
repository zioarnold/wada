package eni.it.gsrestservice.repos.ora;


import eni.it.gsrestservice.entities.oracle.QsFarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:47 </br>
 */
@Repository
public interface QsFarmRepository extends JpaRepository<QsFarm, Long> {
    Optional<QsFarm> findByDescription(String farmName);

    Optional<QsFarm> findByFarmid(String farmId);
}

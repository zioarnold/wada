package eni.it.gsrestservice.repos.post;


import eni.it.gsrestservice.entities.postgres.QsFarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 14:47 </br>
 */
@Repository
public interface QsFarmRepository extends JpaRepository<QsFarm, String> {
    Optional<QsFarm> findByDescription(String farmName);

    @Query(value = "SELECT * FROM QSFARMS WHERE FARMID = :farmId", nativeQuery = true)
    Optional<QsFarm> findByFarmId(String farmId);
}

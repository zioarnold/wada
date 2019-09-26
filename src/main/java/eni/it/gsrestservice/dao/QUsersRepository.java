package eni.it.gsrestservice.dao;

import eni.it.gsrestservice.model.QUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QUsersRepository extends JpaRepository<QUsers, String> {

}

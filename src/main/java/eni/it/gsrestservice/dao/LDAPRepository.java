package eni.it.gsrestservice.dao;

import eni.it.gsrestservice.model.LDAPUsers;
import org.springframework.stereotype.Repository;

@Repository
public interface LDAPRepository extends org.springframework.data.repository.Repository<LDAPUsers, String> {
}

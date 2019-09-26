package eni.it.gsrestservice.dao;

import eni.it.gsrestservice.model.LDAPUsers;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LDAPRepository extends LdapRepository<LDAPUsers> {
    LDAPUsers findByUsername(String username);

    LDAPUsers findByUsernameAndPassword(String username, String password);

    List<LDAPUsers> findByUsernameLikeIgnoreCase(String username);
}

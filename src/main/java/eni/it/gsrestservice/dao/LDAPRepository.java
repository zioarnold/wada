package eni.it.gsrestservice.dao;

import eni.it.gsrestservice.model.LDAPUsers;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LDAPRepository extends LdapRepository<LDAPUsers> {
    void findLDAPUsersBy(String cn);
//    @Autowired
//    LdapTemplate ldapTemplate = new LdapTemplate();
//
//    LDAPUsers create(LDAPUsers person);
//
//    //EniDesignatedNumber - ENIMatricolaNotes
//    LDAPUsers findByEniDesignatedNumber(String cn);
//
//    void update(LDAPUsers person);
//
//    public void delete(LDAPUsers person) {
//        ldapTemplate.delete(person);
//    }
//
//    public List<LDAPUsers> findAll() {
//        return ldapTemplate.findAll(LDAPUsers.class);
//    }
//
//    public List<LDAPUsers> findByLastName(String lastName) {
//        return ldapTemplate.find(query().where("sn").is(lastName), LDAPUsers.class);
//    }
}

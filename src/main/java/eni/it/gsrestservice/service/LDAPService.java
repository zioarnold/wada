package eni.it.gsrestservice.service;

import eni.it.gsrestservice.dao.LDAPRepository;
import eni.it.gsrestservice.model.LDAPUsers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LDAPService {
    private final LDAPRepository ldapRepository;

    public LDAPService(LDAPRepository ldapRepository) {
        this.ldapRepository = ldapRepository;
    }

    public List<LDAPUsers> findAllUsersFromLDAP() {
        List<LDAPUsers> users = new ArrayList<>();
        for (LDAPUsers u : ldapRepository.findAll()) {
            users.add(u);
        }
        return users;
    }
}

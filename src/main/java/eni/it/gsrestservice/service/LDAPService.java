package eni.it.gsrestservice.service;


import eni.it.gsrestservice.model.LDAPConnector;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Zio Arnold aka Arni
 * @created 11/04/2025 - 17:35 </br>
 */
@Service
public class LDAPService extends LDAPConnector {
    public LDAPService(Environment environment) {
        super(environment);
    }
}

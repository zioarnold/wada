package eni.it.gsrestservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GsRestServiceApplicationTests {
    LdapTemplate ldapTemplate;

    @Test
    public void contextLoads() {
    }

    @Test
    public void givenLdapClient_whenCorrectCredentials_thenSuccessfulLogin() {
    }

}

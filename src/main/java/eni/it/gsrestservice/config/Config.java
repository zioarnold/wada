package eni.it.gsrestservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class Config {
    //Vds section
    @Value("${vds.ctx.factory}")
    public static String vdsCtxFactory;
    @Value("${vds.ldapURL}")
    public static String ldapURL;
    @Value("${vds.userName}")
    public static String vdsUserName;
    @Value("${vds.password}")
    public static String vdsPassword;
    @Value("${vds.baseDN}")
    public static String baseDN;

    //Log Section
    @Value("${log.discard}")
    public static String logDiscard;
    @Value("${log.user.role.discarded}")
    public static String logUserRoleDiscarded;
    @Value("${log.role.exist.for.user}")
    public static String logRoleExistForUser;

    //Roles config
    @Value("${roles.config.json.path}")
    public static String rolesConfigJsonPath;

    //Db Section
    @Value("${db.hostname.main}")
    public static String dbHostnameMain;
    @Value("${db.port.main}")
    public static String dbPortMain;
    @Value("${db.sid.main}")
    public static String dbSidMain;
    @Value("${db.username.main}")
    public static String dbUsernameMain;
    @Value("${db.password.main}")
    public static String dbPasswordMain;
    @Value("${db.qs.admin.users}")
    public static String dbQsAdminUsersTbl;
    @Value("${db.qs.farms}")
    public static String dbQsFarmsTbl;
    @Value("${db.tabuser}")
    public static String dbTabUsersTbl;
    @Value("${db.tabattrib}")
    public static String dbTabAttribTbl;


    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}



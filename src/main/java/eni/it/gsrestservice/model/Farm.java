package eni.it.gsrestservice.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Farm {
    public static Long farmId;
    public static String description;
    public static String came;
    public static String dbHost;
    public static String dbUser;
    public static String dbPassword;
    public static String dbPort;
    public static String dbSid;
    public static String qsHost;
    public static String qsHeader;
    public static String qsPathClientJKS;
    public static String qsPathRootJKS;
    public static String qsKeyStorePwd;
    public static String qsXrfKey;
    public static String qsReloadTaskName;
    public static String note;
    public static String environment;
}

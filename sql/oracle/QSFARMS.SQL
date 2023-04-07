CREATE SEQUENCE "WADA"."QSFARMS_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 21 CACHE 20 ORDER NOCYCLE NOKEEP NOSCALE GLOBAL;

create table WADA.QSFARMS
(
    FARMID           NUMBER      default "WADA"."QSFARMS_SEQ"."NEXTVAL" not null,
    DESCRIZIONE      VARCHAR2(100)                                      not null,
    DBUSER           VARCHAR2(30)                                       not null,
    DBPASSWORD       VARCHAR2(30)                                       not null,
    DBHOST           VARCHAR2(100)                                      not null,
    QSHOST           VARCHAR2(100)                                      not null,
    QSPATHCLIENT     VARCHAR2(200)                                      not null,
    QSPATHROOT       VARCHAR2(200)                                      not null,
    QSXRFKEY         VARCHAR2(200)                                      not null,
    QSKSPASSWD       VARCHAR2(30)                                       not null,
    NOTE             VARCHAR2(100),
    DATALASTMODIFY   DATE        default SYSDATE,
    DBSID            VARCHAR2(30)                                       not null,
    DBPORT           VARCHAR2(10)                                       not null,
    QSUSERHEADER     VARCHAR2(100)                                      not null,
    ENVIRONMENT      VARCHAR2(5) default 'DEV'                          not null,
    CAME             VARCHAR2(30)                                       not null,
    QSRELOADTASKNAME VARCHAR2(100)
);
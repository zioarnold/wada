CREATE SEQUENCE "WADA"."QSADMINUSERS_SEQ" MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 21 CACHE 20 ORDER NOCYCLE NOKEEP NOSCALE GLOBAL;

create table WADA.QSADMINUSERS
(
    ID                         NUMBER(10)  default WADA.QSADMINUSERS_SEQ.nextval not null
        constraint QSADMINUSERS_PK
            primary key,
    USERNAME                   VARCHAR2(30)                                      not null
        constraint QSADMINUSERS_UK
            unique,
    PASSWORD                   VARCHAR2(40)                                      not null,
    CURRENT_SESSION_LOGIN_TIME DATE        default sysdate                       not null,
    SESSION_LOGIN_EXPIRE_TIME  DATE        default sysdate                       not null,
    AUTHENTICATED              VARCHAR2(1) default 'N'                           not null,
    ROLE                       VARCHAR2(5) default 'MODER'                       not null
);
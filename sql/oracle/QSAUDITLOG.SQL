create table WADA.QSAUDITLOG
(
    ID             NUMBER generated as identity
        constraint QSAUDITLOG_PK
            primary key,
    DESCRIPTION    VARCHAR2(4000),
    EXECUTION_DATA DATE default sysdate not null
);
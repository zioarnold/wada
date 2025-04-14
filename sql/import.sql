create sequence public.qs_dev_users_seq;

alter sequence public.qs_dev_users_seq owner to postgres;

create sequence public.qs_users_seq;

alter sequence public.qs_users_seq owner to postgres;

create sequence public.qs_users_attrib_seq;

alter sequence public.qs_users_attrib_seq owner to postgres;

create sequence public.qs_dev_users_attrib_seq;

alter sequence public.qs_dev_users_attrib_seq owner to postgres;

create sequence public.qs_admin_users_seq;

alter sequence public.qs_admin_users_seq owner to wada;

create sequence public.qs_audit_log_id_seq;

alter sequence public.qs_audit_log_id_seq owner to wada;

alter sequence public.qs_audit_log_id_seq owned by public.qs_audit_log.id;

create sequence public.qs_farms_seq;

alter sequence public.qs_farms_seq owner to postgres;

create sequence public.qs_farms_tmp_seq;

alter sequence public.qs_farms_tmp_seq owner to postgres;

create table public.qs_dev_users
(
    id               bigint                   default nextval('qs_dev_users_seq'::regclass) not null
        constraint qs_dev_users_pk
            primary key,
    userid           varchar(20)                                                            not null,
    name             varchar(100),
    user_is_active   varchar(1)                                                             not null,
    data_last_modify timestamp with time zone default now()                                 not null
);

alter table public.qs_dev_users
    owner to wada;

create table public.qs_users
(
    id               bigint                   default nextval('qs_users_seq'::regclass) not null
        constraint qs_users_pk
            primary key,
    userid           varchar(20)                                                        not null,
    name             varchar(100),
    user_is_active   varchar(1)                                                         not null,
    data_last_modify timestamp with time zone default now()                             not null
);

alter table public.qs_users
    owner to wada;

create table public.qs_users_attrib
(
    id               bigint                   default nextval('qs_users_attrib_seq'::regclass) not null
        constraint qs_users_attrib_qs_users_id_fk
            references public.qs_users,
    userid           varchar(20)                                                               not null,
    type             varchar(20)                                                               not null,
    value            varchar(100)                                                              not null,
    data_last_modify timestamp with time zone default now()                                    not null
);

alter table public.qs_users_attrib
    owner to wada;

create unique index qs_users_attrib_userid_idx
    on public.qs_users_attrib (userid, type, value);

create table public.qs_dev_users_attrib
(
    id               bigint                   default nextval('qs_dev_users_attrib_seq'::regclass) not null
        constraint qs_dev_users_attrib_qs_dev_users_id_fk
            references public.qs_dev_users,
    userid           varchar(20)                                                                   not null,
    type             varchar(20)                                                                   not null,
    value            varchar(100)                                                                  not null,
    data_last_modify timestamp with time zone default now()                                        not null
);

alter table public.qs_dev_users_attrib
    owner to wada;

create unique index qs_dev_users_attrib_userid_idx
    on public.qs_dev_users_attrib (userid, type, value);

create table public.qs_admin_users
(
    id                         bigint     default nextval('qs_admin_users_seq'::regclass) not null
        primary key,
    username                   varchar(30)                                                not null
        unique,
    password                   varchar(40)                                                not null,
    current_session_login_time timestamp  default CURRENT_TIMESTAMP                       not null,
    session_login_expire_time  timestamp  default CURRENT_TIMESTAMP                       not null,
    authenticated              char       default 'N'::bpchar                             not null,
    role                       varchar(5) default 'MODER'::character varying              not null
);

alter table public.qs_admin_users
    owner to wada;

create table public.qs_audit_log
(
    id             bigint generated always as identity
        constraint qs_audit_log_pk
            primary key,
    description    varchar(4000),
    execution_data timestamp default CURRENT_TIMESTAMP not null
);

alter table public.qs_audit_log
    owner to wada;

create table public.qs_farms
(
    id               bigint     default nextval('qs_farms_seq'::regclass) not null
        constraint qs_farms_pk
            primary key,
    descrizione      varchar(100)                                         not null,
    dbuser           varchar(30)                                          not null,
    dbpassword       varchar(30)                                          not null,
    dbhost           varchar(100)                                         not null,
    qshost           varchar(100)                                         not null,
    qspathclient     varchar(200)                                         not null,
    qspathroot       varchar(200)                                         not null,
    qsxrfkey         varchar(200)                                         not null,
    qskspasswd       varchar(30)                                          not null,
    note             varchar(100),
    datalastmodify   timestamp  default CURRENT_TIMESTAMP,
    dbsid            varchar(30)                                          not null,
    dbport           varchar(10)                                          not null,
    qsuserheader     varchar(100)                                         not null,
    environment      varchar(5) default 'DEV'::character varying          not null,
    came             varchar(30)                                          not null,
    qsreloadtaskname varchar(100)
);

alter table public.qs_farms
    owner to wada;

create table public.qs_farms_tmp
(
    id               bigint     default nextval('qs_farms_tmp_seq'::regclass) not null
        constraint qs_farms_tmp_pk
            primary key,
    descrizione      varchar(100)                                             not null,
    dbuser           varchar(30)                                              not null,
    dbpassword       varchar(30)                                              not null,
    dbhost           varchar(100)                                             not null,
    qshost           varchar(100)                                             not null,
    qspathclient     varchar(200)                                             not null,
    qspathroot       varchar(200)                                             not null,
    qsxrfkey         varchar(200)                                             not null,
    qskspasswd       varchar(30)                                              not null,
    note             varchar(100),
    datalastmodify   timestamp  default CURRENT_TIMESTAMP,
    dbsid            varchar(30)                                              not null,
    dbport           varchar(10)                                              not null,
    qsuserheader     varchar(100)                                             not null,
    environment      varchar(5) default 'DEV'::character varying              not null,
    came             varchar(30)                                              not null,
    qsreloadtaskname varchar(100)
);

alter table public.qs_farms_tmp
    owner to wada;


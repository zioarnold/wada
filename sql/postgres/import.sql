drop sequence if exists public.qs_dev_users_attrib_seq;
drop table if exists public.qs_dev_users_attrib;

create table public.qs_dev_users_attrib
(
    id               bigint                   default nextval('qs_dev_users_attrib_seq'::regclass) not null,
    userid           varchar(20)                                                                   not null,
    type             varchar(20)                                                                   not null,
    value            varchar(100)                                                                  not null,
    data_last_modify timestamp with time zone default now()                                        not null
);

create sequence public.qs_dev_users_attrib_seq;
alter sequence public.qs_dev_users_attrib_seq owner to postgres;

drop sequence if exists public.qs_dev_users_seq;
drop table if exists public.qs_dev_users;

create table public.qs_dev_users
(
    id               bigint                   default nextval('qs_dev_users_seq'::regclass) not null,
    userid           varchar(20)                                                            not null,
    name             varchar(100),
    user_is_active   varchar(1)                                                             not null,
    data_last_modify timestamp with time zone default now()                                 not null
);

create sequence public.qs_dev_users_seq;
alter sequence public.qs_dev_users_seq owner to postgres;

drop sequence if exists public.qs_users_seq;
drop table if exists public.qs_users;

create table public.qs_users
(
    id               bigint                   default nextval('qs_users_seq'::regclass) not null,
    userid           varchar(20)                                                        not null,
    name             varchar(100),
    user_is_active   varchar(1)                                                         not null,
    data_last_modify timestamp with time zone default now()                             not null
);

create sequence public.qs_users_seq;
alter sequence public.qs_users_seq owner to postgres;

drop sequence if exists public.qs_users_attrib_seq;
drop table if exists public.qs_users_attrib;

create table public.qs_users_attrib
(
    id               bigint                   default nextval('qs_users_attrib_seq'::regclass) not null,
    userid           varchar(20)                                                               not null,
    type             varchar(20)                                                               not null,
    value            varchar(100)                                                              not null,
    data_last_modify timestamp with time zone default now()                                    not null
);
create sequence public.qs_users_attrib_seq;
alter sequence public.qs_users_attrib_seq owner to postgres;

create unique index qs_dev_users_attrib_userid_idx
    on public.qs_dev_users_attrib (userid, type, value);

create unique index qs_users_attrib_userid_idx
    on public.qs_users_attrib (userid, type, value);
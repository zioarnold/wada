create sequence public.qs_dev_users_attrib;
alter sequence public.qs_dev_users_attrib owner to postgres;

create table public.qs_dev_users_attrib
(
    id               bigint                   default nextval('qs_dev_users_attrib'::regclass) not null,
    userid           varchar(20)                                                               not null,
    type             varchar(20)                                                               not null,
    value            varchar(100)                                                              not null,
    data_last_modify timestamp with time zone default now()                                    not null
);


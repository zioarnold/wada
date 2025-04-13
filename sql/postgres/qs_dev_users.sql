create sequence public.qs_dev_users_seq;
alter sequence public.qs_dev_users_seq owner to postgres;



create table public.qs_dev_users
(
    id     bigint default nextval('qs_dev_users_seq'::regclass) not null,
    userid varchar(20)                                          not null,
    name             varchar(100),
    user_is_active   varchar(1)                             not null,
    data_last_modify timestamp with time zone default now() not null
);
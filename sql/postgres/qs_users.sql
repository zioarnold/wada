create sequence public.qs_users_seq;
alter sequence public.qs_users_seq owner to postgres;

create table public.qs_users
(
    id               integer                  default nextval('qs_users_seq'::regclass),
    userid           varchar(20)                            not null
        constraint qs_users_pkey
            primary key,
    name             varchar(100),
    user_is_active   varchar(1)                             not null,
    data_last_modify timestamp with time zone default now() not null
);
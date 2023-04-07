create table public.qs_users_attrib
(
    userid           varchar(20)                            not null
        constraint qs_users_attrib_fk
            references public.qs_users
            on delete cascade,
    type             varchar(20)                            not null,
    value            varchar(100)                           not null,
    data_last_modify timestamp with time zone default now() not null
);

alter table public.qs_users_attrib
    owner to postgres;

create unique index qs_users_attrib_userid_idx
    on public.qs_users_attrib (userid, type, value);


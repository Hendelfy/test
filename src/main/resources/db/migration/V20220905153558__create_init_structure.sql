create table protocol
(
    id         serial primary key,
    state      varchar not null,
    created_at timestamp      not null,
    created_by  varchar        not null
);

create table document
(
    id            serial primary key,
    name          varchar       not null,
    created_by    varchar       not null,
    created_at    timestamp     not null,
    document_type varchar not null,
    protocol_id   int references protocol (id)
)
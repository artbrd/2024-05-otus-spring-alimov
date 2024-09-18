--liquibase formatted sql

--changeset alimovae:create_table_authors
create table authors
(
    id        bigserial,
    full_name varchar(255),
    primary key (id)
);

--changeset alimovae:create_table_books
create table books
(
    id        bigserial,
    title     varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);
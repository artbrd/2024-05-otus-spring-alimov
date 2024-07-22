--liquibase formatted sql

--changeset alimovae:create_table_comments
create table comments
(
    id      bigserial,
    text    varchar(255),
    book_id bigint references books (id) on delete cascade,
    primary key (id)
);

--changeset alimovae:insert_data_table_comments
insert into comments(text, book_id)
values ('Comment_1', 1),
       ('Comment_2', 1),
       ('Comment_3', 1),
       ('Comment_4', 2),
       ('Comment_5', 2),
       ('Comment_6', 3);
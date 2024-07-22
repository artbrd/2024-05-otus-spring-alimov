--liquibase formatted sql

--changeset alimovae:insert_data_table_authors
insert into authors(full_name)
values ('Author_1'),
       ('Author_2'),
       ('Author_3');

--changeset alimovae:insert_data_table_genres
insert into genres(name)
values ('Genre_1'),
       ('Genre_2'),
       ('Genre_3'),
       ('Genre_4'),
       ('Genre_5');

--changeset alimovae:insert_data_table_books
insert into books(title, author_id)
values ('BookTitle_1', 1),
       ('BookTitle_2', 2),
       ('BookTitle_3', 3);

--changeset alimovae:insert_data_table_books_genres
insert into books_genres(book_id, genre_id)
values (1, 1),
       (1, 2),
       (2, 3),
       (2, 4),
       (3, 5);

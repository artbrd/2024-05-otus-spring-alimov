--liquibase formatted sql

--changeset alimovae:create_table_otus_users
create table otus_users
(
    id       bigserial,
    username varchar(255) UNIQUE,
    password varchar(255),
    primary key (id)
);

--changeset alimovae:create_table_otus_roles
create table otus_roles
(
    id   bigserial,
    name varchar(255),
    primary key (id)
);

--changeset alimovae:create_table_users_roles
create table users_roles
(
    user_id bigint references otus_users (id) on delete cascade,
    role_id bigint references otus_roles (id) on delete cascade,
    primary key (user_id, role_id)
);

--changeset alimovae:insert_data_table_otus_users
insert into otus_users(username, password)
values ('user', '$2a$10$zgVcXf8XFxMnuum68jFVR.Ndhnx9i89yps7v.Zyp.Xw/DXJFMYYxC'),
       ('admin', '$2a$10$ZUbMe8KLQQNhtua/ZFpsVOPQbv0aDGIXkVJ/33JsuTFYh6U4faJQO');

--changeset alimovae:insert_data_table_otus_roles
insert into otus_roles(name)
values ('USER'),
       ('ADMIN');

--changeset alimovae:insert_data_table_users_roles
insert into users_roles(user_id, role_id)
values (1, 1),
       (2, 2);
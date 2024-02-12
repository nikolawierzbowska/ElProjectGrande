create table users
(
    id                uuid not null primary key,
    email             varchar(30) unique,
    first_name        varchar(15) not null,
    last_name         varchar(20) not null,
    password          varchar(15) not null,
    repeated_password varchar(15) not null
);

create table roles
(
    id   uuid not null primary key,
    name varchar(10)
);

create table courses
(
    id   uuid not null primary key,
    name varchar(50) not null
);

create table opinions
(
    courses_id  uuid  references courses,
    id          uuid not null primary key,
    users_id    uuid not null references users,
    description varchar(200) not null,
    user_name  varchar(50) not null
);

create table users_roles
(
    role_id uuid not null references roles,
    user_id uuid not null references users,
    primary key (role_id, user_id)
);


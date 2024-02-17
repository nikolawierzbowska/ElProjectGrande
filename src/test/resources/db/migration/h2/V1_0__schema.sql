create table users
(
    id                UUID DEFAULT random_uuid()  primary key,
    email             varchar(30) not null unique,
    first_name        varchar(15) not null,
    last_name         varchar(20) not null,
    password          varchar(100) not null,
    repeated_password varchar(100) not null
);

create table roles
(
    id    UUID DEFAULT random_uuid()  primary key,
    name varchar(10)
);

create table courses
(
    id    UUID DEFAULT random_uuid()  primary key,
    name varchar(50) not null
);

create table opinions
(
    courses_id  uuid  references courses,
    id          UUID DEFAULT random_uuid()  primary key,
    description varchar(180) not null,
    user_name  varchar(50) not null
);

create table users_roles
(
    role_id uuid not null references roles,
    user_id uuid not null references users,
    primary key (role_id, user_id)
);

ALTER TABLE courses
    ADD CONSTRAINT name_constraint
        CHECK (
                length(name) <= 50
            );

ALTER TABLE opinions
    ADD CONSTRAINT description_constraint
        CHECK (
                length(description) <= 180
            );

ALTER TABLE users
    ADD CONSTRAINT password_constraint
        CHECK (
                length(password) >= 8
            )



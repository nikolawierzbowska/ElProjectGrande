create table users
(
    id                UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    email             varchar(30) not null unique,
    first_name        varchar(15) not null,
    last_name         varchar(20) not null,
    password          varchar(100) not null,
    repeated_password varchar(100) not null
);

create table roles
(
    id  UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name varchar(10) NOT NULL UNIQUE
);

create table courses
(
    id   UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name varchar(50) not null UNIQUE
);

create table opinions
(   courseS_id uuid,
    FOREIGN KEY (courses_id) REFERENCES  courses (id),
    id          UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    description varchar(200) not null,
    user_name  varchar(15) not null
);

create table users_roles
(
    user_id UUID,
    role_id UUID,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id),
    UNIQUE (user_id, role_id)
);


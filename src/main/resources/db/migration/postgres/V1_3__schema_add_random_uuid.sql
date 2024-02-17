ALTER TABLE users
    ALTER COLUMN id SET DEFAULT gen_random_uuid(),
    ALTER COLUMN id TYPE UUID USING gen_random_uuid()::UUID,
    ADD CONSTRAINT users_id_unique primary key (id);

ALTER TABLE roles
    ALTER COLUMN id  SET DEFAULT gen_random_uuid(),
    ALTER COLUMN id TYPE UUID USING gen_random_uuid()::UUID,
    ALTER COLUMN name TYPE VARCHAR(10),
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT roles_name_unique UNIQUE (name);

ALTER TABLE courses
    ALTER COLUMN id SET DEFAULT gen_random_uuid(),
    ALTER COLUMN id TYPE UUID USING gen_random_uuid()::UUID,
    ADD CONSTRAINT courses_id_unique primary key (id);

ALTER TABLE opinions
    ALTER COLUMN id SET DEFAULT gen_random_uuid(),
    ALTER COLUMN id TYPE UUID USING gen_random_uuid()::UUID,
    ALTER COLUMN description type varchar (180),
    ADD CONSTRAINT opinions_id_unique primary key (id);
ALTER TABLE courses
    ADD CONSTRAINT name_constraint
        CHECK (
                length(name) <= 50
            );

ALTER TABLE opinions
    ADD CONSTRAINT description_constraint
        CHECK (
                length(description) <= 200
            );

ALTER TABLE users
    ADD CONSTRAINT password_constraint
        CHECK (
                length(password) >= 8
            )


INSERT INTO users(id, email, first_name, last_name, password, repeated_password)
VALUES ('d9c21f6e-c541-4d34-9293-961976204294', 'an@test.com','Anna', 'Nowak',
        '$2a$10$3M.RqgFxw2P1d0/00bl74elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3q',
        '$8a$10$3M.ZqgFxw2P1d0/00bl54elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3a'),
       ('d063ff50-00a1-4f96-94f9-a4fb31d33e76', 'jn@test.com','Jan', 'Nowak',
        '$8f$10$3M.ZqgFxf2P1d0/00bl54flBZqjzQwOFRz3zZdU2W9n0sEFqy4r3a',
        '$1a$10$3M.ZqeFxw2P1d2/00bl54elBZqjzQwOFRz3zZdU2W9n0sEFqy4r3d'),
       ('04a4c4b8-c361-4681-bd4b-13c332f0ba38', 'deleted@test.com', '*****', '*****',
        '************************************************************',
        '************************************************************');

INSERT INTO roles(id, name)
VALUES ('68466722-a5a7-49da-af86-b2dcbd6b8203', 'ADMIN'),
       ('b8081184-c21c-40ce-b806-32cdf73a82db', 'USER'),
       ('f8081184-c21c-40ce-b806-32cdf73a82da', 'USER7');

INSERT INTO courses(id, name)
VALUES ('d9c21f6e-c541-4d34-9293-961976204294', 'KURS Z MATEMATYKI');
VALUES ('f8081184-c21c-40ce-b806-32cdf73a82da', 'KURS');


INSERT INTO opinions(courses_id, id, description, user_name)
VALUES ('d9c21f6e-c541-4d34-9293-961976204294', '68466722-a5a7-49da-af86-b2dcbd6b8203','opinia kursu',
        'Anna');


INSERT INTO users_roles(role_id, user_id)
VALUES ('68466722-a5a7-49da-af86-b2dcbd6b8203', 'd9c21f6e-c541-4d34-9293-961976204294');
VALUES ('b8081184-c21c-40ce-b806-32cdf73a82db', 'd063ff50-00a1-4f96-94f9-a4fb31d33e76');

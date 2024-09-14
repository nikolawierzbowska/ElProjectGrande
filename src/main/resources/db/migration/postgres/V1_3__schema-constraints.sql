
DROP TABLE forgot_password;

create table forgot_password
(
    fpid UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    otp INTEGER NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES users(id)

)
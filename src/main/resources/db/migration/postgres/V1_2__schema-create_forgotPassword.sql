create table forgot_password
(
    fpid SERIAL PRIMARY KEY,
    otp INTEGER NOT NULL,
    expiration_time TIMESTAMP NOT NULL,
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES users(id)

)
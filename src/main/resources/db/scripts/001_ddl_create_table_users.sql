CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    phone VARCHAR NOT NULL UNIQUE
);

ALTER TABLE users ADD CONSTRAINT email_unique UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT phone_unique UNIQUE (phone);

COMMENT ON TABLE users IS 'Пользователи';
COMMENT ON COLUMN users.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.username IS 'Имя';
COMMENT ON COLUMN users.email IS 'Почта';
COMMENT ON COLUMN users.phone IS 'Номер телефона';
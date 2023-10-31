CREATE TABLE roles (
    id              NUMERIC PRIMARY KEY,
    name            VARCHAR
);

COMMENT ON TABLE roles IS 'Идентификатор пользователя';
COMMENT ON COLUMN roles.name IS 'Роль пользователя';
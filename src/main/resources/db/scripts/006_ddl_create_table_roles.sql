CREATE TABLE roles (
    id              NUMERIC PRIMARY KEY,
    name            VARCHAR
);

COMMENT ON TABLE metrics IS 'Идентификатор пользователя';
COMMENT ON COLUMN metrics.name IS 'Роль пользователя';
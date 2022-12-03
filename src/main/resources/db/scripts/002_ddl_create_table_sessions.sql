CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    name TEXT,
    poster TEXT
);

COMMENT ON TABLE sessions IS 'Сеансы';
COMMENT ON COLUMN sessions.id IS 'Идентификатор сеанса';
COMMENT ON COLUMN sessions.name IS 'Название фильма';
COMMENT ON COLUMN sessions.poster IS 'Постер';
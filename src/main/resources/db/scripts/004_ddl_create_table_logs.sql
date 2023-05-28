CREATE TABLE logs (
    id              SERIAL PRIMARY KEY,
    level           VARCHAR,
    created         TIMESTAMP,
    message         TEXT,
    class_name      VARCHAR
);

COMMENT ON TABLE logs IS 'Таблица журналирования';
COMMENT ON COLUMN logs.id IS 'Идентификатор';
COMMENT ON COLUMN logs.level IS 'Уровень логирования';
COMMENT ON COLUMN logs.created IS 'Дата и время записи';
COMMENT ON COLUMN logs.message IS 'Запись журнала';
COMMENT ON COLUMN logs.class_name IS 'Имя класса';
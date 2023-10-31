CREATE TABLE metrics (
    id              SERIAL PRIMARY KEY,
    created         TIMESTAMP,
    code            VARCHAR,
    name            VARCHAR,
    value           NUMERIC
);

COMMENT ON TABLE metrics IS 'Таблица метрик мониторинга';
COMMENT ON COLUMN metrics.id IS 'Идентификатор';
COMMENT ON COLUMN metrics.created IS 'Дата и время записи';
COMMENT ON COLUMN metrics.code IS 'Код метрики';
COMMENT ON COLUMN metrics.name IS 'Название метрики';
COMMENT ON COLUMN metrics.value IS 'Значение метрики';
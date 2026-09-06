
-- Esquema inicial de weekly-service-tracker.
-- Refleja el schema que ya estaba en uso en desarrollo. Se usa CREATE TABLE
-- IF NOT EXISTS para que la migracion sea segura tanto en una base vacia
-- (produccion) como en una que ya tiene las tablas (entorno local).

CREATE TABLE IF NOT EXISTS usuarios (
    id             BIGSERIAL    PRIMARY KEY,
    nombre_usuario VARCHAR(50)  NOT NULL UNIQUE,
    pin_hash       TEXT         NOT NULL
);

CREATE TABLE IF NOT EXISTS servicios (
    id              BIGSERIAL      PRIMARY KEY,
    id_usuario      BIGINT         NOT NULL REFERENCES usuarios (id),
    fecha_servicio  DATE           NOT NULL,
    codigo_servicio VARCHAR(50)    NOT NULL,
    distrito        VARCHAR(100)   NOT NULL,
    tipo_servicio   VARCHAR(50)    NOT NULL,
    peaje           BOOLEAN        NOT NULL DEFAULT FALSE,
    monto_peaje     NUMERIC(10, 2),
    monto_servicio  NUMERIC(10, 2) NOT NULL,
    created_at      TIMESTAMP      DEFAULT now()
);
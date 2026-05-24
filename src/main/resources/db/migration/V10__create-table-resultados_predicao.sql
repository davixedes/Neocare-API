CREATE TABLE resultados_predicao (
    id         BIGSERIAL PRIMARY KEY,
    medicao_id BIGINT        NOT NULL UNIQUE,
    score      DOUBLE PRECISION NOT NULL,
    predicao   VARCHAR(50)   NOT NULL,
    analisado_em TIMESTAMP   NOT NULL,
    CONSTRAINT fk_resultados_predicao_medicao
        FOREIGN KEY (medicao_id)
            REFERENCES medicoes(id)
            ON DELETE CASCADE
);

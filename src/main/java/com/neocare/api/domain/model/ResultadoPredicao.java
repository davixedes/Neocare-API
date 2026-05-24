package com.neocare.api.domain.model;

import java.time.LocalDateTime;

public class ResultadoPredicao {

    private Long id;
    private Long medicaoId;
    private Double score;
    private String predicao;
    private LocalDateTime analisadoEm;

    public ResultadoPredicao(Long medicaoId, Double score, String predicao, LocalDateTime analisadoEm) {
        this.medicaoId = medicaoId;
        this.score = score;
        this.predicao = predicao;
        this.analisadoEm = analisadoEm;
    }

    public ResultadoPredicao(Long id, Long medicaoId, Double score, String predicao, LocalDateTime analisadoEm) {
        this.id = id;
        this.medicaoId = medicaoId;
        this.score = score;
        this.predicao = predicao;
        this.analisadoEm = analisadoEm;
    }

    public Long getId() { return id; }
    public Long getMedicaoId() { return medicaoId; }
    public Double getScore() { return score; }
    public String getPredicao() { return predicao; }
    public LocalDateTime getAnalisadoEm() { return analisadoEm; }
}

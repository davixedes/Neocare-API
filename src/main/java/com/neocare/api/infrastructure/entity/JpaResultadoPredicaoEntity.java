package com.neocare.api.infrastructure.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "resultados_predicao")
public class JpaResultadoPredicaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicao_id", nullable = false, unique = true)
    private Long medicaoId;

    @Column(name = "score", nullable = false)
    private Double score;

    @Column(name = "predicao", nullable = false, length = 50)
    private String predicao;

    @Column(name = "analisado_em", nullable = false)
    private LocalDateTime analisadoEm;

    public JpaResultadoPredicaoEntity() {}

    public JpaResultadoPredicaoEntity(Long medicaoId, Double score, String predicao, LocalDateTime analisadoEm) {
        this.medicaoId = medicaoId;
        this.score = score;
        this.predicao = predicao;
        this.analisadoEm = analisadoEm;
    }

    public Long getId() { return id; }

    public Long getMedicaoId() { return medicaoId; }
    public void setMedicaoId(Long medicaoId) { this.medicaoId = medicaoId; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getPredicao() { return predicao; }
    public void setPredicao(String predicao) { this.predicao = predicao; }

    public LocalDateTime getAnalisadoEm() { return analisadoEm; }
    public void setAnalisadoEm(LocalDateTime analisadoEm) { this.analisadoEm = analisadoEm; }
}

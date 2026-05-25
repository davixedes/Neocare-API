package com.neocare.api.infrastructure.entity;

import com.neocare.api.domain.enums.Categoria;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "metricas_estresse")
public class JpaMetricaEstresseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicao_estresse_id", nullable = false, unique = true)
    private JpaMedicaoPsicofisiologicaEntity medicaoPsicofisiologicaEntity;

    @Column(name = "indice_estresse", nullable = false)
    private Integer indiceEstresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 50)
    private Categoria categoria;

    @Column(name = "data_metrica", nullable = false)
    private LocalDateTime dataMetrica;

    public JpaMetricaEstresseEntity() {
    }

    public JpaMetricaEstresseEntity(JpaMedicaoPsicofisiologicaEntity medicaoPsicofisiologicaEntity, Integer indiceEstresse, Categoria categoria, LocalDateTime dataMetrica) {
        this.medicaoPsicofisiologicaEntity = medicaoPsicofisiologicaEntity;
        this.indiceEstresse = indiceEstresse;
        this.categoria = categoria;
        this.dataMetrica = dataMetrica;
    }

    public Long getId() {
        return id;
    }

    public JpaMedicaoPsicofisiologicaEntity getMedicaoPsicofisiologicaEntity() {
        return medicaoPsicofisiologicaEntity;
    }

    public void setMedicaoPsicofisiologicaEntity(JpaMedicaoPsicofisiologicaEntity medicaoPsicofisiologicaEntity) {
        this.medicaoPsicofisiologicaEntity = medicaoPsicofisiologicaEntity;
    }

    public Integer getIndiceEstresse() {
        return indiceEstresse;
    }

    public void setIndiceEstresse(Integer indiceEstresse) {
        this.indiceEstresse = indiceEstresse;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getDataMetrica() {
        return dataMetrica;
    }

    public void setDataMetrica(LocalDateTime dataMetrica) {
        this.dataMetrica = dataMetrica;
    }
}

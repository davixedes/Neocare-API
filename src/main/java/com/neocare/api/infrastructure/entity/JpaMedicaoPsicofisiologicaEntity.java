package com.neocare.api.infrastructure.entity;

import com.neocare.api.domain.enums.TipoMedicao;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Table(name = "medicoes_psicofisiologicas")
@Entity
@PrimaryKeyJoinColumn(name = "medicao_id")
public class JpaMedicaoPsicofisiologicaEntity extends JpaMedicaoEntity {

    private Double variacaoFrequenciaCardiaca;

    private Double condutividadePele;

    public JpaMedicaoPsicofisiologicaEntity() {
    }

    public JpaMedicaoPsicofisiologicaEntity(JpaUsuarioEntity jpaUsuarioEntity, JpaDispositivoEntity jpaDispositivoEntity, LocalDateTime dataMedicao, TipoMedicao tipoMedicao, Double variacaoFrequenciaCardiaca, Double condutividadePele) {
        super(jpaUsuarioEntity, jpaDispositivoEntity, dataMedicao, tipoMedicao);
        this.variacaoFrequenciaCardiaca = variacaoFrequenciaCardiaca;
        this.condutividadePele = condutividadePele;
    }

    public Double getVariacaoFrequenciaCardiaca() {
        return variacaoFrequenciaCardiaca;
    }

    public void setVariacaoFrequenciaCardiaca(Double variacaoFrequenciaCardiaca) {
        this.variacaoFrequenciaCardiaca = variacaoFrequenciaCardiaca;
    }

    public Double getCondutividadePele() {
        return condutividadePele;
    }

    public void setCondutividadePele(Double condutividadePele) {
        this.condutividadePele = condutividadePele;
    }
}

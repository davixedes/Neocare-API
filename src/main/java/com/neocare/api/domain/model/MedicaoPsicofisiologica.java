package com.neocare.api.domain.model;

import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.domain.exception.ValidacaoDominioException;

import java.time.LocalDateTime;

public class MedicaoPsicofisiologica extends Medicao {

    private Double variacaoFrequenciaCardiaca;
    private Double condutividadePele;

    public MedicaoPsicofisiologica(Long idUsuario, Long idDispositivo, TipoMedicao tipoMedicao,
                                    Double variacaoFrequenciaCardiaca, Double condutividadePele) {
        super(idUsuario, idDispositivo, tipoMedicao);
        validar(variacaoFrequenciaCardiaca, condutividadePele);
        this.variacaoFrequenciaCardiaca = variacaoFrequenciaCardiaca;
        this.condutividadePele = condutividadePele;
    }

    public MedicaoPsicofisiologica(Long id, Long idUsuario, Long idDispositivo, LocalDateTime dataMedicao,
                                    TipoMedicao tipoMedicao, Double variacaoFrequenciaCardiaca, Double condutividadePele) {
        super(id, idUsuario, idDispositivo, dataMedicao, tipoMedicao);
        validar(variacaoFrequenciaCardiaca, condutividadePele);
        this.variacaoFrequenciaCardiaca = variacaoFrequenciaCardiaca;
        this.condutividadePele = condutividadePele;
    }

    private void validar(Double hrv, Double gsr) {
        if (hrv != null && (hrv < 0.0 || hrv > 300.0)) {
            throw new ValidacaoDominioException("Variação da frequência cardíaca fora do intervalo válido (0-300 ms)");
        }
        if (gsr != null && (gsr < 0.0 || gsr > 50.0)) {
            throw new ValidacaoDominioException("Condutividade da pele fora do intervalo válido (0-50 μS)");
        }
    }

    public Double getVariacaoFrequenciaCardiaca() { return variacaoFrequenciaCardiaca; }

    public void setVariacaoFrequenciaCardiaca(Double variacaoFrequenciaCardiaca) {
        validar(variacaoFrequenciaCardiaca, this.condutividadePele);
        this.variacaoFrequenciaCardiaca = variacaoFrequenciaCardiaca;
    }

    public Double getCondutividadePele() { return condutividadePele; }

    public void setCondutividadePele(Double condutividadePele) {
        validar(this.variacaoFrequenciaCardiaca, condutividadePele);
        this.condutividadePele = condutividadePele;
    }
}

package com.neocare.api.interfaces.dto.output;

public class MedicaoPsicofisiologicaOutDto {
    private Double variacaoFrequenciaCardiaca;
    private Double condutividadePele;
    private MedicaoOutDto medicaoOutDto;
    private ResultadoPredicaoOutDto resultadoPredicao;

    public MedicaoPsicofisiologicaOutDto(Double variacaoFrequenciaCardiaca, Double condutividadePele,
                                          MedicaoOutDto medicaoOutDto, ResultadoPredicaoOutDto resultadoPredicao) {
        this.variacaoFrequenciaCardiaca = variacaoFrequenciaCardiaca;
        this.condutividadePele = condutividadePele;
        this.medicaoOutDto = medicaoOutDto;
        this.resultadoPredicao = resultadoPredicao;
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

    public MedicaoOutDto getMedicaoOutDto() {
        return medicaoOutDto;
    }

    public void setMedicaoOutDto(MedicaoOutDto medicaoOutDto) {
        this.medicaoOutDto = medicaoOutDto;
    }

    public ResultadoPredicaoOutDto getResultadoPredicao() {
        return resultadoPredicao;
    }

    public void setResultadoPredicao(ResultadoPredicaoOutDto resultadoPredicao) {
        this.resultadoPredicao = resultadoPredicao;
    }
}

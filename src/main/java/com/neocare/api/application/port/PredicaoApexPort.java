package com.neocare.api.application.port;

import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;

public interface PredicaoApexPort {
    ResultadoPredicao analisar(MedicaoPsicofisiologica medicao);
    ResultadoPredicao analisar(MedicaoVital medicao);
}

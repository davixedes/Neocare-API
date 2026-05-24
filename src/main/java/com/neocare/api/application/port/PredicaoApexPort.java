package com.neocare.api.application.port;

import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;

public interface PredicaoApexPort {
    ResultadoPredicao analisar(MedicaoEstresse medicao);
    ResultadoPredicao analisar(MedicaoVital medicao);
}

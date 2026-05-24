package com.neocare.api.application.usecase.predicao;

import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;

import java.util.Optional;

public interface AnalisarMedicaoUseCase {
    Optional<ResultadoPredicao> executarParaEstresse(MedicaoEstresse medicao);
    Optional<ResultadoPredicao> executarParaVital(MedicaoVital medicao);
}

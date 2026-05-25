package com.neocare.api.application.usecase.predicao;

import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;

import java.util.Optional;

public interface AnalisarMedicaoUseCase {
    Optional<ResultadoPredicao> executarParaPsicofisiologica(MedicaoPsicofisiologica medicao);
    Optional<ResultadoPredicao> executarParaVital(MedicaoVital medicao);
}

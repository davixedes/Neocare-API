package com.neocare.api.application.usecase.predicao;

import com.neocare.api.domain.model.ResultadoPredicao;

import java.util.List;
import java.util.Map;

public interface BuscarPredicoesPorMedicaoIdsUseCase {
    Map<Long, ResultadoPredicao> execute(List<Long> medicaoIds);
}

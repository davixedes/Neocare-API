package com.neocare.api.application.usecase.predicao;

import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.repository.ResultadoPredicaoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BuscarPredicoesPorMedicaoIdsUseCaseImpl implements BuscarPredicoesPorMedicaoIdsUseCase {

    private final ResultadoPredicaoRepository resultadoPredicaoRepository;

    public BuscarPredicoesPorMedicaoIdsUseCaseImpl(ResultadoPredicaoRepository resultadoPredicaoRepository) {
        this.resultadoPredicaoRepository = resultadoPredicaoRepository;
    }

    @Override
    public Map<Long, ResultadoPredicao> execute(List<Long> medicaoIds) {
        Map<Long, ResultadoPredicao> mapa = new HashMap<>();
        if (medicaoIds == null) return mapa;
        for (Long id : medicaoIds) {
            if (id == null) continue;
            resultadoPredicaoRepository.porMedicaoId(id).ifPresent(r -> mapa.put(id, r));
        }
        return mapa;
    }
}

package com.neocare.api.domain.repository;

import com.neocare.api.domain.model.ResultadoPredicao;

import java.util.Optional;

public interface ResultadoPredicaoRepository {
    ResultadoPredicao salvar(ResultadoPredicao resultado);
    Optional<ResultadoPredicao> porMedicaoId(Long medicaoId);
}

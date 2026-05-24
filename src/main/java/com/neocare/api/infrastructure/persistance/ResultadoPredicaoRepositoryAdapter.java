package com.neocare.api.infrastructure.persistance;

import com.neocare.api.domain.logging.Logger;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.repository.ResultadoPredicaoRepository;
import com.neocare.api.infrastructure.entity.JpaResultadoPredicaoEntity;
import com.neocare.api.infrastructure.exception.InfraestruturaException;
import com.neocare.api.infrastructure.repository.JpaResultadoPredicaoRepository;

import java.util.Optional;

public class ResultadoPredicaoRepositoryAdapter implements ResultadoPredicaoRepository {

    private final JpaResultadoPredicaoRepository jpaResultadoPredicaoRepository;
    private final Logger logger;

    public ResultadoPredicaoRepositoryAdapter(JpaResultadoPredicaoRepository jpaResultadoPredicaoRepository, Logger logger) {
        this.jpaResultadoPredicaoRepository = jpaResultadoPredicaoRepository;
        this.logger = logger;
    }

    @Override
    public ResultadoPredicao salvar(ResultadoPredicao resultado) {
        logger.info("Salvando resultado de predição para medição ID: " + resultado.getMedicaoId());
        JpaResultadoPredicaoEntity entity = new JpaResultadoPredicaoEntity(
                resultado.getMedicaoId(),
                resultado.getScore(),
                resultado.getPredicao(),
                resultado.getAnalisadoEm()
        );
        try {
            JpaResultadoPredicaoEntity saved = jpaResultadoPredicaoRepository.save(entity);
            logger.info("Resultado de predição salvo com sucesso. ID: " + saved.getId());
            return toDomain(saved);
        } catch (Exception e) {
            logger.error("Erro ao salvar resultado de predição: " + e.getMessage(), e);
            throw new InfraestruturaException("Não foi possível salvar o resultado de predição.", e);
        }
    }

    @Override
    public Optional<ResultadoPredicao> porMedicaoId(Long medicaoId) {
        return jpaResultadoPredicaoRepository.findByMedicaoId(medicaoId).map(this::toDomain);
    }

    private ResultadoPredicao toDomain(JpaResultadoPredicaoEntity entity) {
        return new ResultadoPredicao(
                entity.getId(),
                entity.getMedicaoId(),
                entity.getScore(),
                entity.getPredicao(),
                entity.getAnalisadoEm()
        );
    }
}

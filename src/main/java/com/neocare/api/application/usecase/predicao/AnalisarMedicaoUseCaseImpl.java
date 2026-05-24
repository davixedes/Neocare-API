package com.neocare.api.application.usecase.predicao;

import com.neocare.api.application.port.PredicaoApexPort;
import com.neocare.api.domain.logging.Logger;
import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.repository.ResultadoPredicaoRepository;

import java.util.Optional;

public final class AnalisarMedicaoUseCaseImpl implements AnalisarMedicaoUseCase {

    private final PredicaoApexPort predicaoApexPort;
    private final ResultadoPredicaoRepository resultadoPredicaoRepository;
    private final Logger logger;

    public AnalisarMedicaoUseCaseImpl(PredicaoApexPort predicaoApexPort,
                                      ResultadoPredicaoRepository resultadoPredicaoRepository,
                                      Logger logger) {
        this.predicaoApexPort = predicaoApexPort;
        this.resultadoPredicaoRepository = resultadoPredicaoRepository;
        this.logger = logger;
    }

    @Override
    public Optional<ResultadoPredicao> executarParaEstresse(MedicaoEstresse medicao) {
        try {
            logger.info("Enviando medição de estresse ID " + medicao.getId() + " para análise no Oracle APEX.");
            ResultadoPredicao resultado = predicaoApexPort.analisar(medicao);
            ResultadoPredicao salvo = resultadoPredicaoRepository.salvar(resultado);
            logger.info("Resultado de predição salvo. Predição: " + salvo.getPredicao() + " | Score: " + salvo.getScore());
            return Optional.of(salvo);
        } catch (Exception e) {
            logger.error("Falha ao analisar medição de estresse no APEX. Medição registrada sem resultado de predição. Erro: " + e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<ResultadoPredicao> executarParaVital(MedicaoVital medicao) {
        try {
            logger.info("Enviando medição vital ID " + medicao.getId() + " para análise no Oracle APEX.");
            ResultadoPredicao resultado = predicaoApexPort.analisar(medicao);
            ResultadoPredicao salvo = resultadoPredicaoRepository.salvar(resultado);
            logger.info("Resultado de predição salvo. Predição: " + salvo.getPredicao() + " | Score: " + salvo.getScore());
            return Optional.of(salvo);
        } catch (Exception e) {
            logger.error("Falha ao analisar medição vital no APEX. Medição registrada sem resultado de predição. Erro: " + e.getMessage(), e);
            return Optional.empty();
        }
    }
}

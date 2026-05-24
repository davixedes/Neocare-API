package com.neocare.api.infrastructure.config;

import com.neocare.api.application.usecase.alerta.GerarAlertaPorPredicaoUseCase;
import com.neocare.api.application.usecase.alerta.GerarAlertaPorPredicaoUseCaseImpl;
import com.neocare.api.application.usecase.alerta.ListarAlertasUseCase;
import com.neocare.api.application.usecase.alerta.ListarAlertasUseCaseImpl;
import com.neocare.api.application.usecase.alerta.MarcarAlertaComoLidoUseCase;
import com.neocare.api.application.usecase.alerta.MarcarAlertaComoLidoUseCaseImpl;
import com.neocare.api.domain.logging.Logger;
import com.neocare.api.domain.repository.AlertaRepository;
import com.neocare.api.infrastructure.logging.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlertaUseCasesConfig {

    private final AlertaRepository alertaRepository;

    public AlertaUseCasesConfig(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Bean
    public ListarAlertasUseCase listarAlertasUseCase() {
        return new ListarAlertasUseCaseImpl(alertaRepository);
    }

    @Bean
    public MarcarAlertaComoLidoUseCase marcarAlertaComoLidoUseCase() {
        return new MarcarAlertaComoLidoUseCaseImpl(alertaRepository);
    }

    @Bean
    public GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase() {
        Logger logger = LoggerFactory.getLogger(GerarAlertaPorPredicaoUseCaseImpl.class);
        return new GerarAlertaPorPredicaoUseCaseImpl(alertaRepository, logger);
    }
}

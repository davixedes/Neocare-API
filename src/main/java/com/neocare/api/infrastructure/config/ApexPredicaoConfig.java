package com.neocare.api.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neocare.api.application.port.PredicaoApexPort;
import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCaseImpl;
import com.neocare.api.application.usecase.predicao.BuscarPredicoesPorMedicaoIdsUseCase;
import com.neocare.api.application.usecase.predicao.BuscarPredicoesPorMedicaoIdsUseCaseImpl;
import com.neocare.api.domain.logging.Logger;
import com.neocare.api.domain.repository.ResultadoPredicaoRepository;
import com.neocare.api.infrastructure.client.ApexPredicaoClientAdapter;
import com.neocare.api.infrastructure.logging.LoggerFactory;
import com.neocare.api.infrastructure.persistance.ResultadoPredicaoRepositoryAdapter;
import com.neocare.api.infrastructure.repository.JpaResultadoPredicaoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ApexPredicaoConfig {

    @Value("${apex.predicao.url}")
    private String apexUrl;

    @Value("${apex.predicao.api-key:}")
    private String apiKey;

    @Value("${apex.predicao.timeout-ms:5000}")
    private int timeoutMs;

    @Bean
    public RestTemplate apexRestTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofMillis(timeoutMs))
                .readTimeout(Duration.ofMillis(timeoutMs))
                .build();
    }

    @Bean
    public PredicaoApexPort predicaoApexPort(RestTemplate apexRestTemplate, ObjectMapper objectMapper) {
        return new ApexPredicaoClientAdapter(apexRestTemplate, objectMapper, apexUrl, apiKey);
    }

    @Bean
    public ResultadoPredicaoRepository resultadoPredicaoRepository(JpaResultadoPredicaoRepository jpaResultadoPredicaoRepository) {
        Logger logger = LoggerFactory.getLogger(ResultadoPredicaoRepositoryAdapter.class);
        return new ResultadoPredicaoRepositoryAdapter(jpaResultadoPredicaoRepository, logger);
    }

    @Bean
    public AnalisarMedicaoUseCase analisarMedicaoUseCase(PredicaoApexPort predicaoApexPort,
                                                          ResultadoPredicaoRepository resultadoPredicaoRepository) {
        Logger logger = LoggerFactory.getLogger(AnalisarMedicaoUseCaseImpl.class);
        return new AnalisarMedicaoUseCaseImpl(predicaoApexPort, resultadoPredicaoRepository, logger);
    }

    @Bean
    public BuscarPredicoesPorMedicaoIdsUseCase buscarPredicoesPorMedicaoIdsUseCase(
            ResultadoPredicaoRepository resultadoPredicaoRepository) {
        return new BuscarPredicoesPorMedicaoIdsUseCaseImpl(resultadoPredicaoRepository);
    }
}

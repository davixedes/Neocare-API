package com.neocare.api.infrastructure.client;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neocare.api.application.port.PredicaoApexPort;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class ApexPredicaoClientAdapter implements PredicaoApexPort {

    private static final Logger log = LoggerFactory.getLogger(ApexPredicaoClientAdapter.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String apexUrl;
    private final String apiKey;

    public ApexPredicaoClientAdapter(RestTemplate restTemplate, ObjectMapper objectMapper, String apexUrl, String apiKey) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.apexUrl = apexUrl;
        this.apiKey = apiKey;
    }

    @Override
    public ResultadoPredicao analisar(MedicaoPsicofisiologica medicao) {
        // Discriminador no formato esperado pelo endpoint ORDS do APEX (mantido por compatibilidade do contrato de wire).
        ApexRequest request = new ApexRequest(
                medicao.getId(),
                "MEDICAO_ESTRESSE",
                medicao.getVariacaoFrequenciaCardiaca(),
                medicao.getCondutividadePele(),
                null, null, null, null
        );
        return chamarApex(medicao.getId(), request);
    }

    @Override
    public ResultadoPredicao analisar(MedicaoVital medicao) {
        ApexRequest request = new ApexRequest(
                medicao.getId(),
                "MEDICAO_VITAL",
                null, null,
                medicao.getBatimentosPorMinuto(),
                medicao.getOxigenacaoSangue(),
                medicao.getPressaoSistolica(),
                medicao.getPressaoDiastolica()
        );
        return chamarApex(medicao.getId(), request);
    }

    private ResultadoPredicao chamarApex(Long medicaoId, ApexRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        if (apiKey != null && !apiKey.isBlank()) {
            headers.setBearerAuth(apiKey);
        }

        HttpEntity<ApexRequest> entity = new HttpEntity<>(request, headers);

        log.info("Chamando Oracle APEX para medição ID {} -> {}", medicaoId, apexUrl);
        ResponseEntity<String> rawResponse = restTemplate.exchange(apexUrl, HttpMethod.POST, entity, String.class);
        String rawBody = rawResponse.getBody();
        log.info("Oracle APEX respondeu para medição ID {}: status={}, body={}", medicaoId, rawResponse.getStatusCode(), rawBody);

        if (rawBody == null || rawBody.isBlank()) {
            throw new IllegalStateException("Oracle APEX retornou resposta vazia para medição ID " + medicaoId);
        }

        ApexResponse body = parseResponse(rawBody, medicaoId);

        Double score = body.score();
        String predicao = body.predicao();
        LocalDateTime analisadoEm = body.analisadoEm() != null ? body.analisadoEm() : LocalDateTime.now();

        if (score == null || predicao == null) {
            throw new IllegalStateException(
                    "Oracle APEX retornou payload sem score ou predicao para medição ID " + medicaoId + ". Body=" + rawBody);
        }

        return new ResultadoPredicao(medicaoId, score, predicao, analisadoEm);
    }

    private ApexResponse parseResponse(String rawBody, Long medicaoId) {
        try {
            ApexResponse direct = objectMapper.readValue(rawBody, ApexResponse.class);
            if (direct.score() != null || direct.predicao() != null) {
                return direct;
            }
            // ORDS frequentemente envelopa coleções em { "items": [...] }
            OrdsItemsWrapper wrapper = objectMapper.readValue(rawBody, OrdsItemsWrapper.class);
            if (wrapper.items() != null && !wrapper.items().isEmpty()) {
                return wrapper.items().get(0);
            }
            return direct;
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível parsear resposta do Oracle APEX para medição ID " + medicaoId + ". Body=" + rawBody, e);
        }
    }

    private record ApexRequest(
            Long medicaoId,
            String tipoMedicao,
            Double variacaoFrequenciaCardiaca,
            Double condutividadePele,
            Integer batimentosPorMinuto,
            Double oxigenacaoSangue,
            Integer pressaoSistolica,
            Integer pressaoDiastolica
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ApexResponse(
            @JsonAlias({"SCORE", "Score"}) Double score,
            @JsonAlias({"PREDICAO", "Predicao", "prediction", "PREDICTION"}) String predicao,
            @JsonAlias({"ANALISADO_EM", "AnalisadoEm", "analisado_em", "analyzedAt", "ANALYZED_AT"}) LocalDateTime analisadoEm
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OrdsItemsWrapper(java.util.List<ApexResponse> items) {}
}

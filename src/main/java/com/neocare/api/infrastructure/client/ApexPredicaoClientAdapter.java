package com.neocare.api.infrastructure.client;

import com.neocare.api.application.port.PredicaoApexPort;
import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

public class ApexPredicaoClientAdapter implements PredicaoApexPort {

    private final RestTemplate restTemplate;
    private final String apexUrl;
    private final String apiKey;

    public ApexPredicaoClientAdapter(RestTemplate restTemplate, String apexUrl, String apiKey) {
        this.restTemplate = restTemplate;
        this.apexUrl = apexUrl;
        this.apiKey = apiKey;
    }

    @Override
    public ResultadoPredicao analisar(MedicaoEstresse medicao) {
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
        if (apiKey != null && !apiKey.isBlank()) {
            headers.setBearerAuth(apiKey);
        }

        HttpEntity<ApexRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<ApexResponse> response = restTemplate.exchange(apexUrl, HttpMethod.POST, entity, ApexResponse.class);

        ApexResponse body = response.getBody();
        if (body == null) {
            throw new IllegalStateException("Oracle APEX retornou resposta vazia para medição ID " + medicaoId);
        }

        return new ResultadoPredicao(medicaoId, body.score(), body.predicao(), body.analisadoEm());
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

    private record ApexResponse(
            Double score,
            String predicao,
            LocalDateTime analisadoEm
    ) {}
}

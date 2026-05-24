package com.neocare.api.infrastructure.api.rest;

import com.neocare.api.application.exception.EntidadeNaoEncontradaException;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.domain.repository.ResultadoPredicaoRepository;
import com.neocare.api.interfaces.dto.output.ResultadoPredicaoOutDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resultados-predicao")
public class ResultadoPredicaoRestController {

    private final ResultadoPredicaoRepository resultadoPredicaoRepository;

    public ResultadoPredicaoRestController(ResultadoPredicaoRepository resultadoPredicaoRepository) {
        this.resultadoPredicaoRepository = resultadoPredicaoRepository;
    }

    @GetMapping("/medicao/{medicaoId}")
    public ResponseEntity<ResultadoPredicaoOutDto> porMedicao(@PathVariable Long medicaoId) {
        ResultadoPredicao resultado = resultadoPredicaoRepository.porMedicaoId(medicaoId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Resultado de predição não encontrado para medição ID " + medicaoId));
        return ResponseEntity.ok(new ResultadoPredicaoOutDto(
                resultado.getScore(),
                resultado.getPredicao(),
                resultado.getAnalisadoEm()
        ));
    }
}

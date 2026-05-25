package com.neocare.api.infrastructure.api.rest;

import com.neocare.api.application.usecase.medicao.psicofisiologica.ListarMedicoesPsicofisiologicasUseCase;
import com.neocare.api.application.usecase.medicao.vital.ListarMedicoesVitaisUseCase;
import com.neocare.api.interfaces.controller.MedicaoController;
import com.neocare.api.interfaces.dto.input.MedicaoPsicofisiologicaInDto;
import com.neocare.api.interfaces.dto.input.MedicaoVitalInDto;
import com.neocare.api.interfaces.dto.output.MedicaoPsicofisiologicaOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Adaptador REST para o controller de Medicao.
 * Esta classe contém as anotações específicas do springframework e
 * delega as chamadas para o controller puro.
 */
@RestController
@RequestMapping("/medicoes")
public class MedicaoRestController {

    private final MedicaoController medicaoController;
    private final ListarMedicoesPsicofisiologicasUseCase listarMedicoesPsicofisiologicasUseCase;
    private final ListarMedicoesVitaisUseCase listarMedicoesVitaisUseCase;

    public MedicaoRestController(MedicaoController medicaoController,
                                 ListarMedicoesPsicofisiologicasUseCase listarMedicoesPsicofisiologicasUseCase,
                                 ListarMedicoesVitaisUseCase listarMedicoesVitaisUseCase) {
        this.medicaoController = medicaoController;
        this.listarMedicoesPsicofisiologicasUseCase = listarMedicoesPsicofisiologicasUseCase;
        this.listarMedicoesVitaisUseCase = listarMedicoesVitaisUseCase;
    }

    @PostMapping("/medicao_psicofisiologica")
    public ResponseEntity<MedicaoPsicofisiologicaOutDto> registrarMedicaoPsicofisiologica(@Valid @RequestBody MedicaoPsicofisiologicaInDto inDto, UriComponentsBuilder uriComponentsBuilder){
        final MedicaoPsicofisiologicaOutDto outDto = medicaoController.registrarMedicaoPsicofisiologica(inDto);
        URI uri = uriComponentsBuilder.path("/medicoes/medicao_psicofisiologica/{id}").buildAndExpand(outDto.getMedicaoOutDto().getId()).toUri();
        return ResponseEntity.created(uri).body(outDto);
    }

    @PostMapping("/medicao_vital")
    public ResponseEntity<MedicaoVitalOutDto> registrarMedicaoVital(@Valid @RequestBody MedicaoVitalInDto medicaoVitalInDto, UriComponentsBuilder uriComponentsBuilder){
        final MedicaoVitalOutDto medicaoVitalOutDto = medicaoController.registrarMedicaoVital(medicaoVitalInDto);
        URI uri = uriComponentsBuilder.path("/medicoes/medicao_vital/{id}").buildAndExpand(medicaoVitalOutDto.medicaoOutDto().getId()).toUri();
        return ResponseEntity.created(uri).body(medicaoVitalOutDto);
    }

    @GetMapping("/psicofisiologicas/usuario/{usuarioId}")
    public ResponseEntity<List<MedicaoPsicofisiologica>> listarMedicoesPsicofisiologicas(@PathVariable Long usuarioId) {
        List<MedicaoPsicofisiologica> medicoes = listarMedicoesPsicofisiologicasUseCase.porUsuario(usuarioId);
        return ResponseEntity.ok(medicoes);
    }

    @GetMapping("/vitais/usuario/{usuarioId}")
    public ResponseEntity<List<MedicaoVital>> listarMedicoesVitais(@PathVariable Long usuarioId) {
        List<MedicaoVital> medicoes = listarMedicoesVitaisUseCase.porUsuario(usuarioId);
        return ResponseEntity.ok(medicoes);
    }
}

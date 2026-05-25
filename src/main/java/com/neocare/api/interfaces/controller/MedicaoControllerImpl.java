package com.neocare.api.interfaces.controller;

import com.neocare.api.application.usecase.alerta.GerarAlertaPorPredicaoUseCase;
import com.neocare.api.application.usecase.medicao.psicofisiologica.RegistrarMedicaoPsicofisiologicaUseCase;
import com.neocare.api.application.usecase.medicao.vital.RegistrarMedicaoVitalUseCase;
import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.domain.enums.TipoAlerta;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.interfaces.assembler.MedicaoOutputAssembler;
import com.neocare.api.interfaces.dto.input.MedicaoPsicofisiologicaInDto;
import com.neocare.api.interfaces.dto.input.MedicaoVitalInDto;
import com.neocare.api.interfaces.dto.output.MedicaoPsicofisiologicaOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;
import com.neocare.api.interfaces.mapper.MedicaoPsicofisiologicaMapper;
import com.neocare.api.interfaces.mapper.MedicaoVitalMapper;

public final class MedicaoControllerImpl implements MedicaoController {

    private final RegistrarMedicaoPsicofisiologicaUseCase registrarMedicaoPsicofisiologica;
    private final RegistrarMedicaoVitalUseCase registrarMedicaoVitalUseCase;
    private final AnalisarMedicaoUseCase analisarMedicaoUseCase;
    private final GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase;
    private final MedicaoOutputAssembler assembler;

    public MedicaoControllerImpl(RegistrarMedicaoPsicofisiologicaUseCase registrarMedicaoPsicofisiologica,
                                  RegistrarMedicaoVitalUseCase registrarMedicaoVitalUseCase,
                                  AnalisarMedicaoUseCase analisarMedicaoUseCase,
                                  GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase,
                                  MedicaoOutputAssembler assembler) {
        this.registrarMedicaoPsicofisiologica = registrarMedicaoPsicofisiologica;
        this.registrarMedicaoVitalUseCase = registrarMedicaoVitalUseCase;
        this.analisarMedicaoUseCase = analisarMedicaoUseCase;
        this.gerarAlertaPorPredicaoUseCase = gerarAlertaPorPredicaoUseCase;
        this.assembler = assembler;
    }

    @Override
    public MedicaoPsicofisiologicaOutDto registrarMedicaoPsicofisiologica(MedicaoPsicofisiologicaInDto inDto) {
        MedicaoPsicofisiologica registrada = registrarMedicaoPsicofisiologica.execute(MedicaoPsicofisiologicaMapper.toModel(inDto));
        ResultadoPredicao resultadoPredicao = analisarMedicaoUseCase.executarParaPsicofisiologica(registrada).orElse(null);
        if (resultadoPredicao != null) {
            gerarAlertaPorPredicaoUseCase.execute(resultadoPredicao, registrada.getIdUsuario(), TipoAlerta.ESTRESSE);
        }
        return assembler.toPsicofisiologicaOutDto(registrada, resultadoPredicao);
    }

    @Override
    public MedicaoVitalOutDto registrarMedicaoVital(MedicaoVitalInDto medicaoVitalInDto) {
        MedicaoVital registrada = registrarMedicaoVitalUseCase.execute(MedicaoVitalMapper.toModel(medicaoVitalInDto));
        ResultadoPredicao resultadoPredicao = analisarMedicaoUseCase.executarParaVital(registrada).orElse(null);
        if (resultadoPredicao != null) {
            gerarAlertaPorPredicaoUseCase.execute(resultadoPredicao, registrada.getIdUsuario(), TipoAlerta.AVC);
        }
        return assembler.toVitalOutDto(registrada, resultadoPredicao);
    }
}

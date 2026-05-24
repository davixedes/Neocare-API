package com.neocare.api.interfaces.controller;

import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.application.usecase.medicao.estresse.RegistrarMedicaoEstresseUseCase;
import com.neocare.api.application.usecase.medicao.vital.RegistrarMedicaoVitalUseCase;
import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.model.ResultadoPredicao;
import com.neocare.api.interfaces.assembler.MedicaoOutputAssembler;
import com.neocare.api.interfaces.dto.input.MedicaoEstresseInDto;
import com.neocare.api.interfaces.dto.input.MedicaoVitalInDto;
import com.neocare.api.interfaces.dto.output.MedicaoEstresseOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;
import com.neocare.api.interfaces.mapper.MedicaoEstresseMapper;
import com.neocare.api.interfaces.mapper.MedicaoVitalMapper;

public final class MedicaoControllerImpl implements MedicaoController {

    private final RegistrarMedicaoEstresseUseCase registrarMedicaoEstresse;
    private final RegistrarMedicaoVitalUseCase registrarMedicaoVitalUseCase;
    private final AnalisarMedicaoUseCase analisarMedicaoUseCase;
    private final MedicaoOutputAssembler assembler;

    public MedicaoControllerImpl(RegistrarMedicaoEstresseUseCase registrarMedicaoEstresse,
                                  RegistrarMedicaoVitalUseCase registrarMedicaoVitalUseCase,
                                  AnalisarMedicaoUseCase analisarMedicaoUseCase,
                                  MedicaoOutputAssembler assembler) {
        this.registrarMedicaoEstresse = registrarMedicaoEstresse;
        this.registrarMedicaoVitalUseCase = registrarMedicaoVitalUseCase;
        this.analisarMedicaoUseCase = analisarMedicaoUseCase;
        this.assembler = assembler;
    }

    @Override
    public MedicaoEstresseOutDto registrarMedicaoEstresse(MedicaoEstresseInDto medicaoEstresseInDto) {
        MedicaoEstresse registrada = registrarMedicaoEstresse.execute(MedicaoEstresseMapper.toModel(medicaoEstresseInDto));
        ResultadoPredicao resultadoPredicao = analisarMedicaoUseCase.executarParaEstresse(registrada).orElse(null);
        return assembler.toEstresseOutDto(registrada, resultadoPredicao);
    }

    @Override
    public MedicaoVitalOutDto registrarMedicaoVital(MedicaoVitalInDto medicaoVitalInDto) {
        MedicaoVital registrada = registrarMedicaoVitalUseCase.execute(MedicaoVitalMapper.toModel(medicaoVitalInDto));
        ResultadoPredicao resultadoPredicao = analisarMedicaoUseCase.executarParaVital(registrada).orElse(null);
        return assembler.toVitalOutDto(registrada, resultadoPredicao);
    }
}

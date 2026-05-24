package com.neocare.api.interfaces.controller;

import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.application.usecase.medicao.estresse.RegistrarMedicaoEstresseUseCase;
import com.neocare.api.application.usecase.medicao.vital.RegistrarMedicaoVitalUseCase;
import com.neocare.api.domain.enums.TipoDispositivo;
import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.domain.model.MedicaoEstresse;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.interfaces.assembler.MedicaoOutputAssembler;
import com.neocare.api.interfaces.dto.input.MedicaoEstresseInDto;
import com.neocare.api.interfaces.dto.input.MedicaoVitalInDto;
import com.neocare.api.interfaces.dto.output.DispositivoMedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoEstresseOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoVitalOutDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MedicaoControllerImplTest {

    @Mock
    private RegistrarMedicaoEstresseUseCase registrarMedicaoEstresse;

    @Mock
    private RegistrarMedicaoVitalUseCase registrarMedicaoVitalUseCase;

    @Mock
    private AnalisarMedicaoUseCase analisarMedicaoUseCase;

    @Mock
    private MedicaoOutputAssembler assembler;

    private MedicaoControllerImpl controller;

    @BeforeEach
    void setUp() {
        controller = new MedicaoControllerImpl(
                registrarMedicaoEstresse, registrarMedicaoVitalUseCase, analisarMedicaoUseCase, assembler
        );
    }

    @Test
    @DisplayName("Deve registrar medição de estresse com orquestração correta")
    void deveRegistrarMedicaoEstresse() {
        MedicaoEstresseInDto inDto = new MedicaoEstresseInDto(1L, 1L, TipoMedicao.MEDICAO_ESTRESSE, 50.0, 5.0);
        MedicaoEstresse salva = new MedicaoEstresse(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_ESTRESSE, 50.0, 5.0);

        DispositivoMedicaoOutDto dispositivoDto = new DispositivoMedicaoOutDto(1L, TipoDispositivo.ESP32, "A4:CF:12:45:AE:CC", true);
        MedicaoOutDto medicaoOutDto = new MedicaoOutDto(1L, "João", dispositivoDto, salva.getDataMedicao(), TipoMedicao.MEDICAO_ESTRESSE);
        MedicaoEstresseOutDto expectedOutDto = new MedicaoEstresseOutDto(50.0, 5.0, medicaoOutDto, null);

        when(registrarMedicaoEstresse.execute(any(MedicaoEstresse.class))).thenReturn(salva);
        when(analisarMedicaoUseCase.executarParaEstresse(salva)).thenReturn(Optional.empty());
        when(assembler.toEstresseOutDto(salva, null)).thenReturn(expectedOutDto);

        MedicaoEstresseOutDto resultado = controller.registrarMedicaoEstresse(inDto);

        assertNotNull(resultado);
        assertEquals(50.0, resultado.getVariacaoFrequenciaCardiaca());
        assertEquals(5.0, resultado.getCondutividadePele());
        assertNotNull(resultado.getMedicaoOutDto());
        assertEquals("João", resultado.getMedicaoOutDto().getNomeUsuario());

        verify(registrarMedicaoEstresse).execute(any(MedicaoEstresse.class));
        verify(analisarMedicaoUseCase).executarParaEstresse(salva);
        verify(assembler).toEstresseOutDto(salva, null);
    }

    @Test
    @DisplayName("Deve registrar medição vital com orquestração correta")
    void deveRegistrarMedicaoVital() {
        MedicaoVitalInDto inDto = new MedicaoVitalInDto(1L, 1L, TipoMedicao.MEDICAO_VITAL, 80, 98.0, 120, 80);
        MedicaoVital salva = new MedicaoVital(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_VITAL, 80, 98.0, 120, 80);

        DispositivoMedicaoOutDto dispositivoDto = new DispositivoMedicaoOutDto(1L, TipoDispositivo.ESP32, "A4:CF:12:45:AE:CC", true);
        MedicaoOutDto medicaoOutDto = new MedicaoOutDto(1L, "João", dispositivoDto, salva.getDataMedicao(), TipoMedicao.MEDICAO_VITAL);
        MedicaoVitalOutDto expectedOutDto = new MedicaoVitalOutDto(medicaoOutDto, 80, 98.0, 120, 80, dispositivoDto, null);

        when(registrarMedicaoVitalUseCase.execute(any(MedicaoVital.class))).thenReturn(salva);
        when(analisarMedicaoUseCase.executarParaVital(salva)).thenReturn(Optional.empty());
        when(assembler.toVitalOutDto(salva, null)).thenReturn(expectedOutDto);

        MedicaoVitalOutDto resultado = controller.registrarMedicaoVital(inDto);

        assertNotNull(resultado);
        assertEquals(80, resultado.batimentosPorMinuto());
        assertEquals(98.0, resultado.oxigenacaoSangue());
        assertEquals(120, resultado.pressaoSistolica());
        assertEquals(80, resultado.pressaoDiastolica());
        assertNotNull(resultado.medicaoOutDto());

        verify(registrarMedicaoVitalUseCase).execute(any(MedicaoVital.class));
        verify(analisarMedicaoUseCase).executarParaVital(salva);
        verify(assembler).toVitalOutDto(salva, null);

    }
}

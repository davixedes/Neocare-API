package com.neocare.api.interfaces.controller;

import com.neocare.api.application.usecase.alerta.GerarAlertaPorPredicaoUseCase;
import com.neocare.api.application.usecase.predicao.AnalisarMedicaoUseCase;
import com.neocare.api.application.usecase.medicao.psicofisiologica.RegistrarMedicaoPsicofisiologicaUseCase;
import com.neocare.api.application.usecase.medicao.vital.RegistrarMedicaoVitalUseCase;
import com.neocare.api.domain.enums.TipoDispositivo;
import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.interfaces.assembler.MedicaoOutputAssembler;
import com.neocare.api.interfaces.dto.input.MedicaoPsicofisiologicaInDto;
import com.neocare.api.interfaces.dto.input.MedicaoVitalInDto;
import com.neocare.api.interfaces.dto.output.DispositivoMedicaoOutDto;
import com.neocare.api.interfaces.dto.output.MedicaoPsicofisiologicaOutDto;
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
    private RegistrarMedicaoPsicofisiologicaUseCase registrarMedicaoPsicofisiologica;

    @Mock
    private RegistrarMedicaoVitalUseCase registrarMedicaoVitalUseCase;

    @Mock
    private AnalisarMedicaoUseCase analisarMedicaoUseCase;

    @Mock
    private GerarAlertaPorPredicaoUseCase gerarAlertaPorPredicaoUseCase;

    @Mock
    private MedicaoOutputAssembler assembler;

    private MedicaoControllerImpl controller;

    @BeforeEach
    void setUp() {
        controller = new MedicaoControllerImpl(
                registrarMedicaoPsicofisiologica, registrarMedicaoVitalUseCase,
                analisarMedicaoUseCase, gerarAlertaPorPredicaoUseCase, assembler
        );
    }

    @Test
    @DisplayName("Deve registrar medição psicofisiológica com orquestração correta")
    void deveRegistrarMedicaoPsicofisiologica() {
        MedicaoPsicofisiologicaInDto inDto = new MedicaoPsicofisiologicaInDto(1L, 1L, TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);
        MedicaoPsicofisiologica salva = new MedicaoPsicofisiologica(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);

        DispositivoMedicaoOutDto dispositivoDto = new DispositivoMedicaoOutDto(1L, TipoDispositivo.ESP32, "A4:CF:12:45:AE:CC", true);
        MedicaoOutDto medicaoOutDto = new MedicaoOutDto(1L, "João", dispositivoDto, salva.getDataMedicao(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA);
        MedicaoPsicofisiologicaOutDto expectedOutDto = new MedicaoPsicofisiologicaOutDto(50.0, 5.0, medicaoOutDto, null);

        when(registrarMedicaoPsicofisiologica.execute(any(MedicaoPsicofisiologica.class))).thenReturn(salva);
        when(analisarMedicaoUseCase.executarParaPsicofisiologica(salva)).thenReturn(Optional.empty());
        when(assembler.toPsicofisiologicaOutDto(salva, null)).thenReturn(expectedOutDto);

        MedicaoPsicofisiologicaOutDto resultado = controller.registrarMedicaoPsicofisiologica(inDto);

        assertNotNull(resultado);
        assertEquals(50.0, resultado.getVariacaoFrequenciaCardiaca());
        assertEquals(5.0, resultado.getCondutividadePele());
        assertNotNull(resultado.getMedicaoOutDto());
        assertEquals("João", resultado.getMedicaoOutDto().getNomeUsuario());

        verify(registrarMedicaoPsicofisiologica).execute(any(MedicaoPsicofisiologica.class));
        verify(analisarMedicaoUseCase).executarParaPsicofisiologica(salva);
        verify(assembler).toPsicofisiologicaOutDto(salva, null);
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

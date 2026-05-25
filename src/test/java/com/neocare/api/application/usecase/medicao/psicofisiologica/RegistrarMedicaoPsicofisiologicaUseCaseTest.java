package com.neocare.api.application.usecase.medicao.psicofisiologica;

import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.domain.model.Alerta;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MetricaEstresse;
import com.neocare.api.domain.repository.AlertaRepository;
import com.neocare.api.domain.repository.MedicaoPsicofisiologicaRepository;
import com.neocare.api.domain.repository.MetricaEstresseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrarMedicaoPsicofisiologicaUseCaseTest {

    @Mock
    private MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository;

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private MetricaEstresseRepository metricaEstresseRepository;

    private RegistrarMedicaoPsicofisiologicaUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarMedicaoPsicofisiologicaUseCaseImpl(medicaoPsicofisiologicaRepository, alertaRepository, metricaEstresseRepository);
    }

    @Test
    @DisplayName("Deve salvar medição psicofisiológica e retornar entidade salva")
    void deveSalvarMedicao() {
        MedicaoPsicofisiologica input = new MedicaoPsicofisiologica(1L, 1L, TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);
        MedicaoPsicofisiologica salva = new MedicaoPsicofisiologica(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);

        when(medicaoPsicofisiologicaRepository.save(input)).thenReturn(salva);
        when(metricaEstresseRepository.save(any(MetricaEstresse.class))).thenAnswer(inv -> inv.getArgument(0));

        MedicaoPsicofisiologica resultado = useCase.execute(input);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(medicaoPsicofisiologicaRepository).save(input);
    }

    @Test
    @DisplayName("Não deve gerar alerta quando valores normais")
    void naoDeveGerarAlertaQuandoValoresNormais() {
        MedicaoPsicofisiologica input = new MedicaoPsicofisiologica(1L, 1L, TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);
        MedicaoPsicofisiologica salva = new MedicaoPsicofisiologica(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);

        when(medicaoPsicofisiologicaRepository.save(input)).thenReturn(salva);
        when(metricaEstresseRepository.save(any(MetricaEstresse.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(input);

        verify(alertaRepository, never()).save(any(Alerta.class));
    }

    @Test
    @DisplayName("Deve gerar alerta quando HRV crítico")
    void deveGerarAlertaQuandoHrvCritico() {
        MedicaoPsicofisiologica input = new MedicaoPsicofisiologica(1L, 1L, TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 10.0, 5.0);
        MedicaoPsicofisiologica salva = new MedicaoPsicofisiologica(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 10.0, 5.0);

        when(medicaoPsicofisiologicaRepository.save(input)).thenReturn(salva);
        when(metricaEstresseRepository.save(any(MetricaEstresse.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(input);

        ArgumentCaptor<Alerta> alertaCaptor = ArgumentCaptor.forClass(Alerta.class);
        verify(alertaRepository).save(alertaCaptor.capture());

        Alerta alertaSalvo = alertaCaptor.getValue();
        assertEquals(1L, alertaSalvo.getUsuarioId());
        assertEquals(1L, alertaSalvo.getMedicaoId());
    }

    @Test
    @DisplayName("Deve gerar alerta quando GSR crítico")
    void deveGerarAlertaQuandoGsrCritico() {
        MedicaoPsicofisiologica input = new MedicaoPsicofisiologica(1L, 1L, TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 16.0);
        MedicaoPsicofisiologica salva = new MedicaoPsicofisiologica(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 16.0);

        when(medicaoPsicofisiologicaRepository.save(input)).thenReturn(salva);
        when(metricaEstresseRepository.save(any(MetricaEstresse.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(input);

        verify(alertaRepository).save(any(Alerta.class));
    }

    @Test
    @DisplayName("Deve calcular e salvar métrica de estresse ao registrar medição")
    void deveCalcularESalvarMetrica() {
        MedicaoPsicofisiologica input = new MedicaoPsicofisiologica(1L, 1L, TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);
        MedicaoPsicofisiologica salva = new MedicaoPsicofisiologica(1L, 1L, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0);

        when(medicaoPsicofisiologicaRepository.save(input)).thenReturn(salva);
        when(metricaEstresseRepository.save(any(MetricaEstresse.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.execute(input);

        ArgumentCaptor<MetricaEstresse> metricaCaptor = ArgumentCaptor.forClass(MetricaEstresse.class);
        verify(metricaEstresseRepository).save(metricaCaptor.capture());

        MetricaEstresse metricaSalva = metricaCaptor.getValue();
        assertEquals(1L, metricaSalva.getMedicaoEstresseId());
        assertNotNull(metricaSalva.getIndiceEstresse());
        assertNotNull(metricaSalva.getCategoria());
    }
}

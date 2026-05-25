package com.neocare.api.application.usecase.medicao.psicofisiologica;

import com.neocare.api.domain.enums.TipoMedicao;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.repository.MedicaoPsicofisiologicaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListarMedicoesPsicofisiologicasUseCaseTest {

    @Mock
    private MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository;

    private ListarMedicoesPsicofisiologicasUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListarMedicoesPsicofisiologicasUseCaseImpl(medicaoPsicofisiologicaRepository);
    }

    @Test
    @DisplayName("Deve retornar lista de medições do usuário")
    void deveRetornarMedicoesDoUsuario() {
        Long usuarioId = 1L;
        List<MedicaoPsicofisiologica> medicoes = List.of(
                new MedicaoPsicofisiologica(1L, usuarioId, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 50.0, 5.0),
                new MedicaoPsicofisiologica(2L, usuarioId, 1L, LocalDateTime.now(), TipoMedicao.MEDICAO_PSICOFISIOLOGICA, 30.0, 8.0)
        );

        when(medicaoPsicofisiologicaRepository.findByUsuarioId(usuarioId)).thenReturn(medicoes);

        List<MedicaoPsicofisiologica> resultado = useCase.porUsuario(usuarioId);

        assertEquals(2, resultado.size());
        verify(medicaoPsicofisiologicaRepository).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando usuário não tem medições")
    void deveRetornarListaVazia() {
        Long usuarioId = 99L;
        when(medicaoPsicofisiologicaRepository.findByUsuarioId(usuarioId)).thenReturn(Collections.emptyList());

        List<MedicaoPsicofisiologica> resultado = useCase.porUsuario(usuarioId);

        assertTrue(resultado.isEmpty());
    }
}

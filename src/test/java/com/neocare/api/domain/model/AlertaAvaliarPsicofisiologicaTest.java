package com.neocare.api.domain.model;

import com.neocare.api.domain.enums.Severidade;
import com.neocare.api.domain.enums.TipoAlerta;
import com.neocare.api.domain.enums.TipoMedicao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AlertaAvaliarPsicofisiologicaTest {

    private MedicaoPsicofisiologica criarMedicao(Long id, Double hrv, Double gsr) {
        return new MedicaoPsicofisiologica(id, 1L, 1L, LocalDateTime.now(),
                TipoMedicao.MEDICAO_PSICOFISIOLOGICA, hrv, gsr);
    }

    @Test
    @DisplayName("Não deve gerar alerta quando HRV e GSR estão normais")
    void naoDeveGerarAlertaQuandoValoresNormais() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 50.0, 5.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve gerar alerta MODERADA quando HRV crítico (< 20) mas não extremo")
    void deveGerarAlertaModeradaQuandoHrvCritico() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 18.0, 5.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);

        assertTrue(resultado.isPresent());
        Alerta alerta = resultado.get();
        assertEquals(Severidade.MODERADA, alerta.getSeveridade());
        assertEquals(TipoAlerta.ESTRESSE, alerta.getTipoAlerta());
        assertEquals(1L, alerta.getUsuarioId());
        assertEquals(1L, alerta.getMedicaoId());
    }

    @Test
    @DisplayName("Deve gerar alerta ALTA quando HRV < 15")
    void deveGerarAlertaAltaQuandoHrvMuitoBaixo() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 10.0, 5.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);

        assertTrue(resultado.isPresent());
        assertEquals(Severidade.ALTA, resultado.get().getSeveridade());
    }

    @Test
    @DisplayName("Deve gerar alerta MODERADA quando GSR crítico (> 10) mas não extremo")
    void deveGerarAlertaModeradaQuandoGsrCritico() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 50.0, 12.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);

        assertTrue(resultado.isPresent());
        assertEquals(Severidade.MODERADA, resultado.get().getSeveridade());
    }

    @Test
    @DisplayName("Deve gerar alerta ALTA quando GSR > 15")
    void deveGerarAlertaAltaQuandoGsrMuitoAlto() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 50.0, 16.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);

        assertTrue(resultado.isPresent());
        assertEquals(Severidade.ALTA, resultado.get().getSeveridade());
    }

    @Test
    @DisplayName("Deve gerar alerta ALTA quando ambos HRV e GSR extremos")
    void deveGerarAlertaAltaQuandoAmbosCriticos() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 10.0, 16.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);

        assertTrue(resultado.isPresent());
        Alerta alerta = resultado.get();
        assertEquals(Severidade.ALTA, alerta.getSeveridade());
        assertNotNull(alerta.getMensagem());
        assertNotNull(alerta.getValorDetectado());
    }

    @Test
    @DisplayName("Não deve gerar alerta quando HRV é nulo")
    void naoDeveGerarAlertaQuandoHrvNulo() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, null, 5.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Não deve gerar alerta quando GSR é nulo")
    void naoDeveGerarAlertaQuandoGsrNulo() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 50.0, null);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Não deve gerar alerta quando ambos são nulos")
    void naoDeveGerarAlertaQuandoAmbosNulos() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, null, null);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Não deve gerar alerta quando HRV exatamente 20 e GSR exatamente 10")
    void naoDeveGerarAlertaNoLimiteSemCritico() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 20.0, 10.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve gerar alerta quando HRV é 19.9 (logo abaixo do limiar)")
    void deveGerarAlertaQuandoHrvLogoAbaixoDoLimiar() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 19.9, 5.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);
        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve gerar alerta quando GSR é 10.1 (logo acima do limiar)")
    void deveGerarAlertaQuandoGsrLogoAcimaDoLimiar() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 50.0, 10.1);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);
        assertTrue(resultado.isPresent());
    }

    @Test
    @DisplayName("Deve incluir valores detectados na mensagem do alerta")
    void deveIncluirValoresDetectados() {
        MedicaoPsicofisiologica medicao = criarMedicao(1L, 10.0, 12.0);
        Optional<Alerta> resultado = Alerta.avaliarPsicofisiologica(medicao);

        assertTrue(resultado.isPresent());
        String valorDetectado = resultado.get().getValorDetectado();
        assertTrue(valorDetectado.contains("HRV=10.0"));
        assertTrue(valorDetectado.contains("GSR=12.0"));
    }
}

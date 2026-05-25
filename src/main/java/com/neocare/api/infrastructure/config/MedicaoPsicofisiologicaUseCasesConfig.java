package com.neocare.api.infrastructure.config;

import com.neocare.api.application.usecase.medicao.psicofisiologica.ListarMedicoesPsicofisiologicasUseCase;
import com.neocare.api.application.usecase.medicao.psicofisiologica.ListarMedicoesPsicofisiologicasUseCaseImpl;
import com.neocare.api.application.usecase.medicao.psicofisiologica.RegistrarMedicaoPsicofisiologicaUseCase;
import com.neocare.api.application.usecase.medicao.psicofisiologica.RegistrarMedicaoPsicofisiologicaUseCaseImpl;
import com.neocare.api.application.usecase.medicao.metrica.CalcularMetricaEstresseUseCase;
import com.neocare.api.application.usecase.medicao.metrica.CalcularMetricaEstresseUseCaseImpl;
import com.neocare.api.domain.repository.AlertaRepository;
import com.neocare.api.domain.repository.MedicaoPsicofisiologicaRepository;
import com.neocare.api.domain.repository.MetricaEstresseRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MedicaoPsicofisiologicaUseCasesConfig {

    private final MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository;
    private final AlertaRepository alertaRepository;
    private final MetricaEstresseRepository metricaEstresseRepository;

    public MedicaoPsicofisiologicaUseCasesConfig(MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository,
                                                  AlertaRepository alertaRepository,
                                                  MetricaEstresseRepository metricaEstresseRepository) {
        this.medicaoPsicofisiologicaRepository = medicaoPsicofisiologicaRepository;
        this.alertaRepository = alertaRepository;
        this.metricaEstresseRepository = metricaEstresseRepository;
    }

    @Bean
    public RegistrarMedicaoPsicofisiologicaUseCase registrarMedicaoPsicofisiologicaUseCase() {
        return new RegistrarMedicaoPsicofisiologicaUseCaseImpl(medicaoPsicofisiologicaRepository, alertaRepository, metricaEstresseRepository);
    }

    @Bean
    public ListarMedicoesPsicofisiologicasUseCase listarMedicoesPsicofisiologicasUseCase() {
        return new ListarMedicoesPsicofisiologicasUseCaseImpl(medicaoPsicofisiologicaRepository);
    }

    @Bean
    public CalcularMetricaEstresseUseCase calcularMetricaEstresseUseCase() {
        return new CalcularMetricaEstresseUseCaseImpl(metricaEstresseRepository);
    }
}

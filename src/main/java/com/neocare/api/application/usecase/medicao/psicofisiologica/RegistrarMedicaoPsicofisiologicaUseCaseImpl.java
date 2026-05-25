package com.neocare.api.application.usecase.medicao.psicofisiologica;

import com.neocare.api.domain.model.Alerta;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.model.MetricaEstresse;
import com.neocare.api.domain.repository.AlertaRepository;
import com.neocare.api.domain.repository.MedicaoPsicofisiologicaRepository;
import com.neocare.api.domain.repository.MetricaEstresseRepository;

public final class RegistrarMedicaoPsicofisiologicaUseCaseImpl implements RegistrarMedicaoPsicofisiologicaUseCase {

    private final MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository;
    private final AlertaRepository alertaRepository;
    private final MetricaEstresseRepository metricaEstresseRepository;

    public RegistrarMedicaoPsicofisiologicaUseCaseImpl(MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository,
                                                       AlertaRepository alertaRepository,
                                                       MetricaEstresseRepository metricaEstresseRepository) {
        this.medicaoPsicofisiologicaRepository = medicaoPsicofisiologicaRepository;
        this.alertaRepository = alertaRepository;
        this.metricaEstresseRepository = metricaEstresseRepository;
    }

    @Override
    public MedicaoPsicofisiologica execute(MedicaoPsicofisiologica medicaoPsicofisiologica) {
        MedicaoPsicofisiologica salva = medicaoPsicofisiologicaRepository.save(medicaoPsicofisiologica);
        Alerta.avaliarPsicofisiologica(salva).ifPresent(alertaRepository::save);

        MetricaEstresse metrica = MetricaEstresse.calcular(
                salva.getId(),
                salva.getVariacaoFrequenciaCardiaca(),
                salva.getCondutividadePele()
        );
        metricaEstresseRepository.save(metrica);

        return salva;
    }
}

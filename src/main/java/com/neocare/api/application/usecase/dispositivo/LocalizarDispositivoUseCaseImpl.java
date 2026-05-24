package com.neocare.api.application.usecase.dispositivo;

import com.neocare.api.domain.model.Dispositivo;
import com.neocare.api.domain.repository.DispositivoRepository;

public final class LocalizarDispositivoUseCaseImpl implements LocalizarDispositivoUseCase{

    private final DispositivoRepository dispositivoRepository;

    public LocalizarDispositivoUseCaseImpl(DispositivoRepository dispositivoRepository) {
        this.dispositivoRepository = dispositivoRepository;
    }

    @Override
    public Dispositivo execute(Long id) {
        return dispositivoRepository.findById(id);
    }
}

package com.neocare.api.infrastructure.persistance;

import com.neocare.api.domain.logging.Logger;
import com.neocare.api.domain.model.MedicaoVital;
import com.neocare.api.domain.repository.MedicaoVitalRepository;
import com.neocare.api.infrastructure.entity.JpaDispositivoEntity;
import com.neocare.api.infrastructure.entity.JpaMedicaoVitalEntity;
import com.neocare.api.infrastructure.entity.JpaUsuarioEntity;
import com.neocare.api.application.exception.EntidadeNaoEncontradaException;
import com.neocare.api.infrastructure.exception.InfraestruturaException;
import com.neocare.api.infrastructure.repository.JpaDispositivoRepository;
import com.neocare.api.infrastructure.repository.JpaMedicaoVitalRepository;
import com.neocare.api.infrastructure.repository.JpaUsuarioRepository;
import com.neocare.api.interfaces.mapper.MedicaoVitalMapper;

public class MedicaoVitalRepositoryAdapter implements MedicaoVitalRepository {

    private final JpaMedicaoVitalRepository jpaMedicaoVitalRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final JpaDispositivoRepository jpaDispositivoRepository;
    private final Logger logger;

    public MedicaoVitalRepositoryAdapter(JpaMedicaoVitalRepository jpaMedicaoVitalRepository, JpaUsuarioRepository jpaUsuarioRepository, JpaDispositivoRepository jpaDispositivoRepository, Logger logger) {
        this.jpaMedicaoVitalRepository = jpaMedicaoVitalRepository;
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.jpaDispositivoRepository = jpaDispositivoRepository;
        this.logger = logger;
    }


    @Override
    public MedicaoVital save(MedicaoVital medicaoVital) {
        logger.info("Salvando medição vital no banco de dados.");
        JpaUsuarioEntity usuarioEntity = jpaUsuarioRepository.findById(medicaoVital.getIdUsuario())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Usuário com ID " + medicaoVital.getIdUsuario() + " não encontrado."));
        JpaDispositivoEntity dispositivoEntity = jpaDispositivoRepository.findById(medicaoVital.getIdDispositivo())
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Dispositivo com ID " + medicaoVital.getIdDispositivo() + " não encontrado."));
        JpaMedicaoVitalEntity entity = MedicaoVitalMapper.toEntity(medicaoVital, usuarioEntity, dispositivoEntity);

        try {
            logger.info("Iniciando operação de salvamento da medição vital.");
            JpaMedicaoVitalEntity savedEntity = jpaMedicaoVitalRepository.save(entity);
            logger.info("Medição vital salva com sucesso. ID: " + medicaoVital.getId());

            return MedicaoVitalMapper.jpaToDomain(savedEntity);
        } catch (Exception e) {
            logger.error("Erro ao salvar medição vital: " + e.getMessage(), e);
            throw new InfraestruturaException("Não foi possível salvar a medição vital.", e);
        }
    }

    @Override
    public java.util.List<MedicaoVital> findByUsuarioId(Long usuarioId) {
        logger.info("Buscando medições vitais do usuário ID: " + usuarioId);
        return jpaMedicaoVitalRepository.findByJpaUsuarioEntityIdOrderByDataMedicaoDesc(usuarioId)
                .stream()
                .map(MedicaoVitalMapper::jpaToDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}

package com.neocare.api.infrastructure.persistance;

import com.neocare.api.domain.logging.Logger;
import com.neocare.api.domain.model.MedicaoPsicofisiologica;
import com.neocare.api.domain.repository.MedicaoPsicofisiologicaRepository;
import com.neocare.api.infrastructure.entity.JpaDispositivoEntity;
import com.neocare.api.infrastructure.entity.JpaMedicaoPsicofisiologicaEntity;
import com.neocare.api.infrastructure.entity.JpaUsuarioEntity;
import com.neocare.api.infrastructure.exception.InfraestruturaException;
import com.neocare.api.infrastructure.repository.JpaDispositivoRepository;
import com.neocare.api.infrastructure.repository.JpaMedicaoPsicofisiologicaRepository;
import com.neocare.api.infrastructure.repository.JpaUsuarioRepository;
import com.neocare.api.interfaces.mapper.MedicaoPsicofisiologicaMapper;

public class MedicaoPsicofisiologicaRepositoryAdapter implements MedicaoPsicofisiologicaRepository {

    private final JpaMedicaoPsicofisiologicaRepository jpaMedicaoPsicofisiologicaRepository;
    private final JpaUsuarioRepository jpaUsuarioRepository;
    private final JpaDispositivoRepository jpaDispositivoRepository;
    private final Logger logger;

    public MedicaoPsicofisiologicaRepositoryAdapter(JpaMedicaoPsicofisiologicaRepository jpaMedicaoPsicofisiologicaRepository,
                                                     JpaUsuarioRepository jpaUsuarioRepository,
                                                     JpaDispositivoRepository jpaDispositivoRepository,
                                                     Logger logger) {
        this.jpaMedicaoPsicofisiologicaRepository = jpaMedicaoPsicofisiologicaRepository;
        this.jpaUsuarioRepository = jpaUsuarioRepository;
        this.jpaDispositivoRepository = jpaDispositivoRepository;
        this.logger = logger;
    }

    @Override
    public MedicaoPsicofisiologica save(MedicaoPsicofisiologica medicaoPsicofisiologica) {
        logger.info("Salvando medição psicofisiológica no banco de dados.");
        JpaUsuarioEntity usuarioEntity = jpaUsuarioRepository.findById(medicaoPsicofisiologica.getIdUsuario())
                .orElseThrow(() -> new InfraestruturaException("Usuário com ID " + medicaoPsicofisiologica.getIdUsuario() + " não encontrado."));
        JpaDispositivoEntity dispositivoEntity = jpaDispositivoRepository.findById(medicaoPsicofisiologica.getIdDispositivo())
                .orElseThrow(() -> new InfraestruturaException("Dispositivo com ID " + medicaoPsicofisiologica.getIdDispositivo() + " não encontrado."));
        JpaMedicaoPsicofisiologicaEntity entity = MedicaoPsicofisiologicaMapper.toEntity(medicaoPsicofisiologica, usuarioEntity, dispositivoEntity);

        try {
            logger.info("Iniciando operação de salvamento da medição psicofisiológica.");
            JpaMedicaoPsicofisiologicaEntity savedEntity = jpaMedicaoPsicofisiologicaRepository.save(entity);
            logger.info("Medição psicofisiológica salva com sucesso. ID: " + savedEntity.getId());

            return MedicaoPsicofisiologicaMapper.jpaToDomain(savedEntity);
        } catch (Exception e) {
            logger.error("Erro ao salvar medição psicofisiológica: " + e.getMessage(), e);
            throw new InfraestruturaException("Não foi possível salvar a medição psicofisiológica.", e);
        }
    }

    @Override
    public java.util.List<MedicaoPsicofisiologica> findByUsuarioId(Long usuarioId) {
        logger.info("Buscando medições psicofisiológicas do usuário ID: " + usuarioId);
        return jpaMedicaoPsicofisiologicaRepository.findByJpaUsuarioEntityIdOrderByDataMedicaoDesc(usuarioId)
                .stream()
                .map(MedicaoPsicofisiologicaMapper::jpaToDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.Optional<MedicaoPsicofisiologica> findById(Long id) {
        logger.info("Buscando medição psicofisiológica por ID: " + id);
        return jpaMedicaoPsicofisiologicaRepository.findById(id)
                .map(MedicaoPsicofisiologicaMapper::jpaToDomain);
    }
}

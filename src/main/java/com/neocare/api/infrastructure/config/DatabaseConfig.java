package com.neocare.api.infrastructure.config;

import com.neocare.api.domain.repository.*;
import com.neocare.api.infrastructure.persistance.*;
import com.neocare.api.infrastructure.repository.*;
import com.neocare.api.domain.logging.Logger;
import com.neocare.api.infrastructure.logging.LoggerFactory;
import com.neocare.api.infrastructure.services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseConfig {

    @Bean
    public UsuarioRepository clienteRepository(JpaUsuarioRepository jpaUsuarioRepository, JpaCredenciaisRepository jpaCredenciaisRepository, JpaRoleRepository jpaRoleRepository) {
        final Logger logger = LoggerFactory.getLogger(UsuarioRepositoryAdapter.class);
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return new UsuarioRepositoryAdapter(jpaUsuarioRepository, jpaCredenciaisRepository, jpaRoleRepository, passwordEncoder, logger);
    }

    @Bean
    public DispositivoRepository dispositivoRepository(JpaDispositivoRepository jpaDispositivoRepository) {
        final Logger logger = LoggerFactory.getLogger(DispositivoRepositoryAdapter.class);
        return new DispositivoRepositoryAdapter(jpaDispositivoRepository, logger);
    }

    @Bean
    public MedicaoPsicofisiologicaRepository medicaoPsicofisiologicaRepository(JpaMedicaoPsicofisiologicaRepository jpaMedicaoPsicofisiologicaRepository, JpaUsuarioRepository jpaUsuarioRepository, JpaDispositivoRepository jpaDispositivoRepository) {
        final Logger logger = LoggerFactory.getLogger(MedicaoPsicofisiologicaRepositoryAdapter.class);
        return new MedicaoPsicofisiologicaRepositoryAdapter(jpaMedicaoPsicofisiologicaRepository, jpaUsuarioRepository, jpaDispositivoRepository, logger);
    }

    @Bean public CredenciaisRepository credenciaisRepository(JpaCredenciaisRepository jpaCredenciaisRepository) {
        final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
        return new CredenciaisRepositoryAdapter(jpaCredenciaisRepository, logger);
    }

    @Bean
    public RoleRepository roleRepository(JpaRoleRepository jpaRoleRepository) {
        final Logger logger = LoggerFactory.getLogger(UsuarioRepositoryAdapter.class);
        return new RoleRepositoryAdapter(jpaRoleRepository, logger);
    }

    @Bean
    public MedicaoVitalRepository medicaoVitalRepository(JpaMedicaoVitalRepository jpaMedicaoVitalRepository, JpaUsuarioRepository jpaUsuarioRepository, JpaDispositivoRepository jpaDispositivoRepository) {
        final Logger logger = LoggerFactory.getLogger(MedicaoVitalRepositoryAdapter.class);
        return new MedicaoVitalRepositoryAdapter(jpaMedicaoVitalRepository, jpaUsuarioRepository, jpaDispositivoRepository, logger);
    }

    @Bean
    public AlertaRepository alertaRepository(JpaAlertaRepository jpaAlertaRepository, JpaUsuarioRepository jpaUsuarioRepository, JpaMedicaoRepository jpaMedicaoRepository) {
        final Logger logger = LoggerFactory.getLogger(AlertaRepositoryAdapter.class);
        return new AlertaRepositoryAdapter(jpaAlertaRepository, jpaUsuarioRepository, jpaMedicaoRepository, logger);
    }

    @Bean
    public MetricaEstresseRepository metricaEstresseRepository(JpaMetricaEstresseRepository jpaMetricaEstresseRepository, JpaMedicaoPsicofisiologicaRepository jpaMedicaoPsicofisiologicaRepository) {
        final Logger logger = LoggerFactory.getLogger(MetricaEstresseRepositoryAdapter.class);
        return new MetricaEstresseRepositoryAdapter(jpaMetricaEstresseRepository, jpaMedicaoPsicofisiologicaRepository, logger);
    }
}

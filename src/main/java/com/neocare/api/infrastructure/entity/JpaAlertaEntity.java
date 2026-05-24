package com.neocare.api.infrastructure.entity;

import com.neocare.api.domain.enums.Severidade;
import com.neocare.api.domain.enums.TipoAlerta;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table(name = "alertas")
@Entity
public class JpaAlertaEntity {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private JpaUsuarioEntity usuarioEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicao_id", nullable = false)
    private JpaMedicaoEntity medicaoEntity;

    @Enumerated(EnumType.STRING)
    private TipoAlerta tipoAlerta;

    private String valorDetectado;

    @Enumerated(EnumType.STRING)
    private Severidade severidade;

    private String mensagem;

    private LocalDateTime dataNotificacao;

    @Column(name = "lido", nullable = false)
    private Boolean lido = Boolean.FALSE;

    public JpaAlertaEntity() {
    }

    public JpaAlertaEntity(JpaUsuarioEntity usuarioEntity, JpaMedicaoEntity medicaoEntity, TipoAlerta tipoAlerta, String valorDetectado, Severidade severidade, String mensagem, LocalDateTime dataNotificacao) {
        this(null, usuarioEntity, medicaoEntity, tipoAlerta, valorDetectado, severidade, mensagem, dataNotificacao, Boolean.FALSE);
    }

    public JpaAlertaEntity(Long id, JpaUsuarioEntity usuarioEntity, JpaMedicaoEntity medicaoEntity, TipoAlerta tipoAlerta, String valorDetectado, Severidade severidade, String mensagem, LocalDateTime dataNotificacao, Boolean lido) {
        this.id = id;
        this.usuarioEntity = usuarioEntity;
        this.medicaoEntity = medicaoEntity;
        this.tipoAlerta = tipoAlerta;
        this.valorDetectado = valorDetectado;
        this.severidade = severidade;
        this.mensagem = mensagem;
        this.dataNotificacao = dataNotificacao;
        this.lido = lido != null ? lido : Boolean.FALSE;
    }

    public Long getId() {
        return id;
    }

    public JpaUsuarioEntity getUsuarioEntity() {
        return usuarioEntity;
    }

    public JpaMedicaoEntity getMedicaoEntity() {
        return medicaoEntity;
    }

    public TipoAlerta getTipoAlerta() {
        return tipoAlerta;
    }

    public String getValorDetectado() {
        return valorDetectado;
    }

    public Severidade getSeveridade() {
        return severidade;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataNotificacao() {
        return dataNotificacao;
    }

    public Boolean getLido() {
        return lido != null ? lido : Boolean.FALSE;
    }

    public void setLido(Boolean lido) {
        this.lido = lido;
    }
}

package com.temnafesta.notificacao.infrastructure.persistence.entity;

import com.temnafesta.notificacao.domain.vo.StatusEnvioEnum;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor()
public class NotificacaoEntity {

    @Id
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "pedido_id", nullable = false, length = 100)
    private String pedidoId;

    @Column(name = "telefone_destino", nullable = false, length = 20)
    private String telefoneDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_evento", nullable = false, length = 50)
    private TipoEventoEnum tipoEvento;

    @Column(name = "data_ocorrencia", nullable = false)
    private LocalDateTime dataOcorrencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_envio", nullable = false, length = 20)
    private StatusEnvioEnum statusEnvio;

    @Column(name = "conteudo_formatado", nullable = false, columnDefinition = "TEXT")
    private String conteudoFormatado;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_processamento")
    private LocalDateTime dataProcessamento;

    @Column(name = "tentativas", nullable = false)
    private int tentativas;
}

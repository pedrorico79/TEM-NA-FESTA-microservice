package com.temnafesta.notificacao.domain.model;

import com.temnafesta.notificacao.domain.vo.StatusEnvioEnum;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class Notificacao {
    UUID id;
    String pedidoId;
    String telefoneDestino;
    TipoEventoEnum tipoEvento;
    LocalDateTime dataOcorrencia;
    StatusEnvioEnum statusEnvio;
    String conteudoFormatado;
    LocalDateTime dataCriacao;
    LocalDateTime dataProcessamento;
    int tentativas;

    public Notificacao marcarComoEnviada() {
        return new Notificacao(
                id, pedidoId, telefoneDestino, tipoEvento, dataOcorrencia,
                StatusEnvioEnum.ENVIADA, conteudoFormatado,
                dataCriacao, LocalDateTime.now(), tentativas
        );
    }

    public Notificacao registrarFalha(int limiteTentativas) {
        int novasTentativas = tentativas + 1;
        StatusEnvioEnum novoStatus = novasTentativas >= limiteTentativas
                ? StatusEnvioEnum.FALHA
                : StatusEnvioEnum.PENDENTE;
        return new Notificacao(
                id, pedidoId, telefoneDestino, tipoEvento, dataOcorrencia,
                novoStatus, conteudoFormatado,
                dataCriacao, LocalDateTime.now(), novasTentativas
        );
    }

    public static Notificacao nova(String pedidoId, String telefoneDestino, TipoEventoEnum tipoEvento, LocalDateTime dataOcorrencia, String conteudoFormatado) {
        return new Notificacao(
                UUID.randomUUID(), pedidoId, telefoneDestino, tipoEvento, dataOcorrencia,
                StatusEnvioEnum.PENDENTE, conteudoFormatado,
                LocalDateTime.now(), null, 0
        );
    }
}
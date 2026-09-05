package com.temnafesta.notificacao.application;

import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;

import java.time.Instant;

public record ReceberEventoNotificacaoCommand(
        String pedidoId,
        TipoEventoEnum tipoEvento,
        String nomeCliente,
        String telefoneDestino,
        String valorTotal,
        Instant dataOcorrencia
) {}
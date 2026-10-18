package com.temnafesta.notificacao.application;

import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record ReceberEventoNotificacaoCommand(
        Long pedidoId,
        TipoEventoEnum tipoEvento,
        Instant dataOcorrencia,
        BigDecimal valorTotal,
        BigDecimal taxaEntrega,
        String observacao,
        String nomeCliente,
        String telefoneDestino,
        List<ItemPedidoEvent> itens
) {
    public record ItemPedidoEvent(
            Long produtoId,
            String nomeProduto,
            Integer quantidade,
            BigDecimal valorUnitario,
            BigDecimal subtotal
    ) {
    }
}
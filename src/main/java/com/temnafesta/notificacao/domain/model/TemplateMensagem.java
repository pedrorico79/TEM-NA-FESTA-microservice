package com.temnafesta.notificacao.domain.model;

import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class TemplateMensagem {
    UUID id;
    TipoEventoEnum tipoEvento;
    String corpoTexto;
    boolean ativo;
    LocalDateTime dataCriacao;
    LocalDateTime dataAtualizacao;

    public String formatarMensagem(
            String nomeCliente,
            String numeroPedido,
            String valorTotal,
            String taxaEntrega,
            String observacao,
            String itens) {
        return corpoTexto
                .replace("{nomeCliente}", nomeCliente)
                .replace("{numeroPedido}", numeroPedido)
                .replace("{valorTotal}", valorTotal)
                .replace("{taxaEntrega}", taxaEntrega)
                .replace("{observacao}", observacao)
                .replace("{itens}", itens);
    }
}
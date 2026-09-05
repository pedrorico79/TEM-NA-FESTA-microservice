package com.temnafesta.notificacao.domain.model;

import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import lombok.Value;

import java.util.UUID;

@Value
public class TemplateMensagem {
    UUID id;
    TipoEventoEnum tipoEvento;
    String corpoTexto;
    boolean ativo;

    public String formatarMensagem(String nomeCliente, String numeroPedido, String valorTotal) {
        return corpoTexto
                .replace("{nomeCliente}", nomeCliente)
                .replace("{numeroPedido}", numeroPedido)
                .replace("{valorTotal}", valorTotal);
    }
}
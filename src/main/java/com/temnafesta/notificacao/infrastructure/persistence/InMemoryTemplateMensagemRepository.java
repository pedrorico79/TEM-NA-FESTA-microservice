package com.temnafesta.notificacao.infrastructure.persistence;

import com.temnafesta.notificacao.domain.model.TemplateMensagem;
import com.temnafesta.notificacao.domain.ports.TemplateMensagemRepositoryPort;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryTemplateMensagemRepository implements TemplateMensagemRepositoryPort {

    private final Map<TipoEventoEnum, TemplateMensagem> templates = new EnumMap<>(TipoEventoEnum.class);

    public InMemoryTemplateMensagemRepository() {
        templates.put(TipoEventoEnum.CRIACAO_PEDIDO, new TemplateMensagem(
                UUID.randomUUID(),
                TipoEventoEnum.CRIACAO_PEDIDO,
                "Olá {nomeCliente}! Seu pedido #{numeroPedido} foi confirmado. "
                        + "Valor total: R$ {valorTotal}. Obrigado pela preferência! - Tem Na Festa",
                true));
        templates.put(TipoEventoEnum.ATUALIZACAO_STATUS, new TemplateMensagem(
                UUID.randomUUID(),
                TipoEventoEnum.ATUALIZACAO_STATUS,
                "Olá {nomeCliente}! Seu pedido #{numeroPedido} teve o status atualizado. "
                        + "Valor: R$ {valorTotal}.",
                true));
    }

    @Override
    public Optional<TemplateMensagem> buscarPorTipoEventoAtivo(TipoEventoEnum tipoEvento) {
        return Optional.ofNullable(templates.get(tipoEvento))
                .filter(TemplateMensagem::isAtivo);
    }
}

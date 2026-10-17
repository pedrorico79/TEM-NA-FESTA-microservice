package com.temnafesta.notificacao.infrastructure.persistence;

import com.temnafesta.notificacao.domain.model.Notificacao;
import com.temnafesta.notificacao.domain.ports.NotificacaoRepositoryPort;
import com.temnafesta.notificacao.domain.vo.StatusEnvioEnum;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryNotificacaoRepository implements NotificacaoRepositoryPort {

    private final ConcurrentMap<UUID, Notificacao> notificacoes = new ConcurrentHashMap<>();

    @Override
    public Notificacao salvar(Notificacao notificacao) {
        notificacoes.put(notificacao.getId(), notificacao);
        return notificacao;
    }

    @Override
    public boolean existeNotificacaoEnviada(String pedidoId, TipoEventoEnum tipoEvento) {
        return notificacoes.values().stream()
                .anyMatch(notificacao ->
                        notificacao.getPedidoId().equals(pedidoId)
                                && notificacao.getTipoEvento() == tipoEvento
                                && notificacao.getStatusEnvio() == StatusEnvioEnum.ENVIADA);
    }

    @Override
    public Optional<Notificacao> buscarPorId(String id) {
        return Optional.ofNullable(notificacoes.get(UUID.fromString(id)));
    }
}

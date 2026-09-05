package com.temnafesta.notificacao.domain.ports;

import com.temnafesta.notificacao.domain.model.Notificacao;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;

import java.util.Optional;

public interface NotificacaoRepositoryPort {
    Notificacao salvar(Notificacao notificacao);

    boolean existeNotificacaoEnviada(String pedidoId, TipoEventoEnum tipoEvento);

    Optional<Notificacao> buscarPorId(String id);
}
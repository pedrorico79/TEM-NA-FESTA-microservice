package com.temnafesta.notificacao.domain.ports;

import com.temnafesta.notificacao.domain.model.TemplateMensagem;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;

import java.util.Optional;

public interface TemplateMensagemRepositoryPort {
    Optional<TemplateMensagem> buscarPorTipoEventoAtivo(TipoEventoEnum tipoEvento);
}
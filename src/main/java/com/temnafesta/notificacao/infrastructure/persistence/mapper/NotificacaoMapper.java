package com.temnafesta.notificacao.infrastructure.persistence.mapper;

import com.temnafesta.notificacao.domain.model.Notificacao;
import com.temnafesta.notificacao.infrastructure.persistence.entity.NotificacaoEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoMapper {

    public NotificacaoEntity toEntity(Notificacao notificacao) {
        NotificacaoEntity entity = new NotificacaoEntity();
        entity.setId(notificacao.getId());
        entity.setPedidoId(notificacao.getPedidoId());
        entity.setTelefoneDestino(notificacao.getTelefoneDestino());
        entity.setTipoEvento(notificacao.getTipoEvento());
        entity.setDataOcorrencia(notificacao.getDataOcorrencia());
        entity.setStatusEnvio(notificacao.getStatusEnvio());
        entity.setConteudoFormatado(notificacao.getConteudoFormatado());
        entity.setDataCriacao(notificacao.getDataCriacao());
        entity.setDataProcessamento(notificacao.getDataProcessamento());
        entity.setTentativas(notificacao.getTentativas());
        return entity;
    }

    public Notificacao toDomain(NotificacaoEntity entity) {
        return new Notificacao(
                entity.getId(),
                entity.getPedidoId(),
                entity.getTelefoneDestino(),
                entity.getTipoEvento(),
                entity.getDataOcorrencia(),
                entity.getStatusEnvio(),
                entity.getConteudoFormatado(),
                entity.getDataCriacao(),
                entity.getDataProcessamento(),
                entity.getTentativas());
    }
}

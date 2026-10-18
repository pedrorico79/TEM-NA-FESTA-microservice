package com.temnafesta.notificacao.infrastructure.persistence.repository;

import com.temnafesta.notificacao.domain.vo.StatusEnvioEnum;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import com.temnafesta.notificacao.infrastructure.persistence.entity.NotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataNotificacaoJpaRepository extends JpaRepository<NotificacaoEntity, UUID> {

    boolean existsByPedidoIdAndTipoEventoAndStatusEnvio(
            String pedidoId,
            TipoEventoEnum tipoEvento,
            StatusEnvioEnum statusEnvio);
}

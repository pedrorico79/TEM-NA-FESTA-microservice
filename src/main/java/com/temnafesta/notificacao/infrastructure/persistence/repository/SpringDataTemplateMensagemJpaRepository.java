package com.temnafesta.notificacao.infrastructure.persistence.repository;

import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import com.temnafesta.notificacao.infrastructure.persistence.entity.TemplateMensagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataTemplateMensagemJpaRepository
        extends JpaRepository<TemplateMensagemEntity, UUID> {

    Optional<TemplateMensagemEntity> findByTipoEventoAndAtivoTrue(TipoEventoEnum tipoEvento);
}

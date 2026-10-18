package com.temnafesta.notificacao.infrastructure.persistence.adapter;

import com.temnafesta.notificacao.domain.model.Notificacao;
import com.temnafesta.notificacao.domain.ports.NotificacaoRepositoryPort;
import com.temnafesta.notificacao.domain.vo.StatusEnvioEnum;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import com.temnafesta.notificacao.infrastructure.persistence.entity.NotificacaoEntity;
import com.temnafesta.notificacao.infrastructure.persistence.mapper.NotificacaoMapper;
import com.temnafesta.notificacao.infrastructure.persistence.repository.SpringDataNotificacaoJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaNotificacaoRepositoryAdapter implements NotificacaoRepositoryPort {

    private final SpringDataNotificacaoJpaRepository repository;
    private final NotificacaoMapper mapper;

    public JpaNotificacaoRepositoryAdapter(SpringDataNotificacaoJpaRepository repository, NotificacaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Notificacao salvar(Notificacao notificacao) {
        NotificacaoEntity entity = mapper.toEntity(notificacao);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public boolean existeNotificacaoEnviada(String pedidoId, TipoEventoEnum tipoEvento) {
        return repository.existsByPedidoIdAndTipoEventoAndStatusEnvio(
                pedidoId,
                tipoEvento,
                StatusEnvioEnum.ENVIADA);
    }

    @Override
    public Optional<Notificacao> buscarPorId(String id) {
        return repository.findById(UUID.fromString(id))
                .map(mapper::toDomain);
    }
}

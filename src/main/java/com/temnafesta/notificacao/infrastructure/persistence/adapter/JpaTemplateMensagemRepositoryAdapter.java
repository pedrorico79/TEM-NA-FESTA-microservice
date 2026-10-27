package com.temnafesta.notificacao.infrastructure.persistence.adapter;

import com.temnafesta.notificacao.domain.model.TemplateMensagem;
import com.temnafesta.notificacao.domain.ports.TemplateMensagemRepositoryPort;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import com.temnafesta.notificacao.infrastructure.persistence.repository.SpringDataTemplateMensagemJpaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaTemplateMensagemRepositoryAdapter implements TemplateMensagemRepositoryPort {

    private final SpringDataTemplateMensagemJpaRepository repository;

    public JpaTemplateMensagemRepositoryAdapter(SpringDataTemplateMensagemJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TemplateMensagem> buscarPorTipoEventoAtivo(TipoEventoEnum tipoEvento) {
        return repository.findByTipoEventoAndAtivoTrue(tipoEvento)
                .map(entity -> new TemplateMensagem(
                        entity.getId(),
                        entity.getTipoEvento(),
                        entity.getCorpoTexto(),
                        entity.isAtivo(),
                        entity.getDataCriacao(),
                        entity.getDataAtualizacao()));
    }
}

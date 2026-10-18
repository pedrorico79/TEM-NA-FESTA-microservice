package com.temnafesta.notificacao.infrastructure.persistence.mapper;

import com.temnafesta.notificacao.domain.model.Notificacao;
import com.temnafesta.notificacao.domain.vo.StatusEnvioEnum;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotificacaoMapperTest {

    private final NotificacaoMapper mapper = new NotificacaoMapper();

    @Test
    void mapeiaNotificacaoEntreDominioEEntidade() {
        LocalDateTime agora = LocalDateTime.of(2026, 10, 18, 19, 0);
        Notificacao notificacao = new Notificacao(
                UUID.randomUUID(),
                "42",
                "+5511999999999",
                TipoEventoEnum.CRIACAO_PEDIDO,
                agora,
                StatusEnvioEnum.ENVIADA,
                "Mensagem",
                agora,
                agora,
                1);

        Notificacao convertida = mapper.toDomain(mapper.toEntity(notificacao));

        assertEquals(notificacao, convertida);
    }
}

package com.temnafesta.notificacao.application.usecase;

import com.temnafesta.notificacao.application.ReceberEventoNotificacaoCommand;
import com.temnafesta.notificacao.domain.model.TemplateMensagem;
import com.temnafesta.notificacao.domain.ports.MensageriaExternaPort;
import com.temnafesta.notificacao.domain.ports.NotificacaoRepositoryPort;
import com.temnafesta.notificacao.domain.ports.TemplateMensagemRepositoryPort;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessarNotificacaoUseCaseTest {

    @Mock
    private NotificacaoRepositoryPort notificacaoRepository;

    @Mock
    private TemplateMensagemRepositoryPort templateRepository;

    @Mock
    private MensageriaExternaPort mensageriaExterna;

    @InjectMocks
    private ProcessarNotificacaoUseCase useCase;

    @Test
    void enviaMensagemComValoresFormatadosEItensDoPedido() {
        TemplateMensagem template = new TemplateMensagem(
                UUID.randomUUID(),
                TipoEventoEnum.PEDIDO_CRIADO,
                "{numeroPedido}|{valorTotal}|{taxaEntrega}|{itens}|{observacao}",
                true,
                LocalDateTime.of(2026, 10, 16, 9, 0),
                LocalDateTime.of(2026, 10, 16, 9, 0));
        when(notificacaoRepository.existeNotificacaoEnviada("42", TipoEventoEnum.PEDIDO_CRIADO))
                .thenReturn(false);
        when(templateRepository.buscarPorTipoEventoAtivo(TipoEventoEnum.PEDIDO_CRIADO))
                .thenReturn(Optional.of(template));

        ReceberEventoNotificacaoCommand command = new ReceberEventoNotificacaoCommand(
                42L,
                TipoEventoEnum.PEDIDO_CRIADO,
                Instant.parse("2026-10-16T12:00:00Z"),
                new BigDecimal("125.50"),
                new BigDecimal("8.75"),
                "Deixar na portaria",
                "Ana",
                "+5511999999999",
                List.of(new ReceberEventoNotificacaoCommand.ItemPedidoEvent(
                        7L, "Produto X", 2, new BigDecimal("58.375"), new BigDecimal("116.75"))));

        useCase.executar(command);

        ArgumentCaptor<String> mensagem = ArgumentCaptor.forClass(String.class);
        verify(mensageriaExterna).enviar(eq("+5511999999999"), mensagem.capture());
        assertTrue(mensagem.getValue().contains("42|"));
        assertTrue(mensagem.getValue().contains("125,50"));
        assertTrue(mensagem.getValue().contains("8,75"));
        assertTrue(mensagem.getValue().contains("Produto X x2"));
        assertTrue(mensagem.getValue().contains("58,38"));
        assertTrue(mensagem.getValue().contains("116,75"));
        assertTrue(mensagem.getValue().contains("Deixar na portaria"));
    }
}

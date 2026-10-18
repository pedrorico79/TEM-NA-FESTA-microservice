package com.temnafesta.notificacao.application.usecase;

import com.temnafesta.notificacao.application.ReceberEventoNotificacaoCommand;
import com.temnafesta.notificacao.domain.model.Notificacao;
import com.temnafesta.notificacao.domain.model.TemplateMensagem;
import com.temnafesta.notificacao.domain.ports.MensageriaExternaPort;
import com.temnafesta.notificacao.domain.ports.NotificacaoRepositoryPort;
import com.temnafesta.notificacao.domain.ports.TemplateMensagemRepositoryPort;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProcessarNotificacaoUseCase {

    private static final int LIMITE_TENTATIVAS = 3;

    private final NotificacaoRepositoryPort notificacaoRepository;
    private final TemplateMensagemRepositoryPort templateRepository;
    private final MensageriaExternaPort mensageriaExterna;

    public ProcessarNotificacaoUseCase(
            NotificacaoRepositoryPort notificacaoRepository,
            TemplateMensagemRepositoryPort templateRepository,
            MensageriaExternaPort mensageriaExterna) {
        this.notificacaoRepository = notificacaoRepository;
        this.templateRepository = templateRepository;
        this.mensageriaExterna = mensageriaExterna;
    }

    public void executar(ReceberEventoNotificacaoCommand command) {
        String pedidoId = command.pedidoId().toString();
        if (notificacaoRepository.existeNotificacaoEnviada(pedidoId, command.tipoEvento())) {
            return;
        }

        TemplateMensagem template = templateRepository.buscarPorTipoEventoAtivo(command.tipoEvento())
                .orElseThrow(() -> new IllegalStateException(
                        "Template não encontrado para o tipo de evento: " + command.tipoEvento()));

        String mensagemFormatada = template.formatarMensagem(
                command.nomeCliente(),
                pedidoId,
                formatarMoeda(command.valorTotal()),
                formatarMoeda(command.taxaEntrega()),
                command.observacao() == null || command.observacao().isBlank()
                        ? "Sem observações"
                        : command.observacao(),
                formatarItens(command.itens())
        );

        LocalDateTime dataOcorrencia = command.dataOcorrencia() != null
                ? LocalDateTime.ofInstant(command.dataOcorrencia(), ZoneOffset.UTC)
                : LocalDateTime.now();

        Notificacao notificacao = Notificacao.nova(
                pedidoId, command.telefoneDestino(), command.tipoEvento(), dataOcorrencia, mensagemFormatada);

        try {
            mensageriaExterna.enviar(command.telefoneDestino(), mensagemFormatada);
            notificacaoRepository.salvar(notificacao.marcarComoEnviada());
        } catch (Exception e) {
            notificacaoRepository.salvar(notificacao.registrarFalha(LIMITE_TENTATIVAS));
            throw new RuntimeException("Falha ao enviar notificação", e);
        }
    }

    private String formatarMoeda(BigDecimal valor) {
        return valor == null
                ? "não informado"
                : NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR")).format(valor);
    }

    private String formatarItens(List<ReceberEventoNotificacaoCommand.ItemPedidoEvent> itens) {
        if (itens == null || itens.isEmpty()) {
            return "Nenhum item informado.";
        }

        return itens.stream()
                .map(item -> {
                    String nomeProduto = item.nomeProduto() == null ? "Produto sem nome" : item.nomeProduto();
                    String quantidade = item.quantidade() == null ? "?" : item.quantidade().toString();
                    return String.format(
                            Locale.forLanguageTag("pt-BR"),
                            "- %s x%s (unitário: %s; subtotal: %s)",
                            nomeProduto,
                            quantidade,
                            formatarMoeda(item.valorUnitario()),
                            formatarMoeda(item.subtotal()));
                })
                .collect(Collectors.joining("\n"));
    }
}
package com.temnafesta.notificacao.application.usecase;

import com.temnafesta.notificacao.application.ReceberEventoNotificacaoCommand;
import com.temnafesta.notificacao.domain.model.Notificacao;
import com.temnafesta.notificacao.domain.model.TemplateMensagem;
import com.temnafesta.notificacao.domain.ports.MensageriaExternaPort;
import com.temnafesta.notificacao.domain.ports.NotificacaoRepositoryPort;
import com.temnafesta.notificacao.domain.ports.TemplateMensagemRepositoryPort;


import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
        if (notificacaoRepository.existeNotificacaoEnviada(command.pedidoId(), command.tipoEvento())) {
            return;
        }

        TemplateMensagem template = templateRepository.buscarPorTipoEventoAtivo(command.tipoEvento())
                .orElseThrow(() -> new IllegalStateException(
                        "Template não encontrado para o tipo de evento: " + command.tipoEvento()));

        String mensagemFormatada = template.formatarMensagem(
                command.nomeCliente(), command.pedidoId(), command.valorTotal());

        LocalDateTime dataOcorrencia = command.dataOcorrencia() != null
                ? LocalDateTime.ofInstant(command.dataOcorrencia(), ZoneOffset.UTC)
                : LocalDateTime.now();

        Notificacao notificacao = Notificacao.nova(
                command.pedidoId(), command.telefoneDestino(), command.tipoEvento(), dataOcorrencia, mensagemFormatada);

        try {
            mensageriaExterna.enviar(command.telefoneDestino(), mensagemFormatada);
            notificacaoRepository.salvar(notificacao.marcarComoEnviada());
        } catch (Exception e) {
            notificacaoRepository.salvar(notificacao.registrarFalha(LIMITE_TENTATIVAS));
            throw new RuntimeException("Falha ao enviar notificação", e);
        }
    }
}
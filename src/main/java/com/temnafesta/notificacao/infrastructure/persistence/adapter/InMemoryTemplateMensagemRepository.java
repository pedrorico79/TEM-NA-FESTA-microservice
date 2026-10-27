package com.temnafesta.notificacao.infrastructure.persistence.adapter;

import com.temnafesta.notificacao.domain.model.TemplateMensagem;
import com.temnafesta.notificacao.domain.ports.TemplateMensagemRepositoryPort;
import com.temnafesta.notificacao.domain.vo.TipoEventoEnum;
import org.springframework.stereotype.Repository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InMemoryTemplateMensagemRepository implements TemplateMensagemRepositoryPort {

    private final Map<TipoEventoEnum, TemplateMensagem> templates = new EnumMap<>(TipoEventoEnum.class);

    public InMemoryTemplateMensagemRepository() {

        templates.put(TipoEventoEnum.PEDIDO_CRIADO,
                new TemplateMensagem(
                        UUID.randomUUID(),
                        TipoEventoEnum.PEDIDO_CRIADO,
                        "Olá {nomeCliente}! Seu pedido #{numeroPedido} foi registrado e está aguardando o pagamento do sinal. "
                                + "Valor total: {valorTotal}. Taxa de entrega: {taxaEntrega}. "
                                + "Itens:\n{itens}\nObservação: {observacao}. "
                                + "Assim que o sinal for confirmado, iniciaremos o processamento do pedido. "
                                + "Obrigado pela preferência! - Tem Na Festa",
                        true
                ));

        templates.put(TipoEventoEnum.PEDIDO_CONFIRMADO,
                new TemplateMensagem(
                        UUID.randomUUID(),
                        TipoEventoEnum.PEDIDO_CONFIRMADO,
                        "Olá {nomeCliente}! Seu pedido #{numeroPedido} foi confirmado. "
                                + "Valor total: {valorTotal}. Taxa de entrega: {taxaEntrega}. "
                                + "Itens:\n{itens}\nObservação: {observacao}. "
                                + "Em breve iniciaremos a produção do seu pedido. "
                                + "Obrigado pela preferência! - Tem Na Festa",
                        true
                ));

        templates.put(TipoEventoEnum.EM_PRODUCAO,
                new TemplateMensagem(
                        UUID.randomUUID(),
                        TipoEventoEnum.EM_PRODUCAO,
                        "Olá {nomeCliente}! Seu pedido #{numeroPedido} entrou em produção. "
                                + "Nossa equipe já iniciou a preparação dos itens solicitados. "
                                + "Obrigado pela preferência! - Tem Na Festa",
                        true
                ));

        templates.put(TipoEventoEnum.PRONTO_PARA_ENTREGA,
                new TemplateMensagem(
                        UUID.randomUUID(),
                        TipoEventoEnum.PRONTO_PARA_ENTREGA,
                        "Olá {nomeCliente}! Seu pedido #{numeroPedido} está pronto para entrega. "
                                + "Em breve entraremos em contato para alinhar os detalhes finais. "
                                + "Obrigado pela preferência! - Tem Na Festa",
                        true
                ));

        templates.put(TipoEventoEnum.ENTREGUE,
                new TemplateMensagem(
                        UUID.randomUUID(),
                        TipoEventoEnum.ENTREGUE,
                        "Olá {nomeCliente}! Seu pedido #{numeroPedido} foi entregue com sucesso. "
                                + "Agradecemos pela confiança e esperamos atendê-lo novamente em uma próxima ocasião. "
                                + "Obrigado pela preferência! - Tem Na Festa",
                        true
                ));

        templates.put(TipoEventoEnum.CANCELADO,
                new TemplateMensagem(
                        UUID.randomUUID(),
                        TipoEventoEnum.CANCELADO,
                        "Olá {nomeCliente}! Informamos que o pedido #{numeroPedido} foi cancelado. "
                                + "Caso tenha alguma dúvida, entre em contato com nossa equipe. "
                                + "Obrigado pela preferência! - Tem Na Festa",
                        true
                ));
    }

    @Override
    public Optional<TemplateMensagem> buscarPorTipoEventoAtivo(TipoEventoEnum tipoEvento) {
        return Optional.ofNullable(templates.get(tipoEvento))
                .filter(TemplateMensagem::isAtivo);
    }
}

package com.temnafesta.notificacao.infrastructure.messaging;

import com.temnafesta.notificacao.application.ReceberEventoNotificacaoCommand;
import com.temnafesta.notificacao.application.usecase.ProcessarNotificacaoUseCase;
import com.temnafesta.notificacao.infrastructure.config.RabbitBeanConfiguration;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PedidoConsumer {

    private final ProcessarNotificacaoUseCase processarNotificacaoUseCase;

    @RabbitListener(queues = RabbitBeanConfiguration.QUEUE_PEDIDO)
    public void printarPedido(ReceberEventoNotificacaoCommand evento) {
        processarNotificacaoUseCase.executar(evento);
    }
}

package com.temnafesta.notificacao.infrastructure.messaging;

import com.temnafesta.notificacao.domain.ports.MensageriaExternaPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogMensageriaExternaAdapter implements MensageriaExternaPort {

    private static final Logger logger = LoggerFactory.getLogger(LogMensageriaExternaAdapter.class);

    @Override
    public void enviar(String telefoneDestino, String mensagem) {
        logger.info("Envio externo simulado; destino e conteúdo omitidos dos logs.");
    }
}

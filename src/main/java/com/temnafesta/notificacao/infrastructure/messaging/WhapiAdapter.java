package com.temnafesta.notificacao.infrastructure.messaging;

import com.temnafesta.notificacao.domain.ports.MensageriaExternaPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.util.Map;

@Component
public class WhapiAdapter implements MensageriaExternaPort {

    private final RestClient client;

    private static final Logger LOGGER = LoggerFactory.getLogger(WhapiAdapter.class);

    public WhapiAdapter(@Value("${whapi.token}") String token) {
        this.client = RestClient.builder()
                .baseUrl("https://gate.whapi.cloud")
                .defaultHeader("Authorization", "Bearer " + token)
                .build();

    }

    @Override
    public void enviar(String telefoneDestino, String mensagem) {

        LOGGER.info("Iniciando o envio do evento para '{}'", telefoneDestino);

        Map<String, String> payload = Map.of(
                "to", telefoneDestino,
                "body", mensagem
        );

        try {
            client.post()
                    .uri("/messages/text") // caminho especifico para envio de mensagens no Whapi
                    .body(payload)
                    .retrieve()// Executa a requisição e prepara a leitura da resposta da API, incluindo o tratamento de erros HTTP.
                    .toBodilessEntity(); // Conclui a chamada e recebe a resposta HTTP sem precisar guardar o conteúdo do corpo da resposta.

            LOGGER.info("Evento enviado com sucesso");

        } catch (RestClientResponseException e) {
            LOGGER.error(
                    "Whapi retornou erro HTTP {} ao enviar mensagem para '{}': {}",
                    e.getStatusCode(),
                    telefoneDestino,
                    e.getResponseBodyAsString(),
                    e
            );
            throw e;

        } catch (RestClientException e) {
            LOGGER.error("Falha de comunicação com a Whapi ao enviar mensagem para '{}'", telefoneDestino, e);
            throw e;

        }
    }

}

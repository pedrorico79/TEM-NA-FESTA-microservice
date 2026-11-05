package com.temnafesta.notificacao.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Verificação de saúde usada pelo Docker Swarm (healthcheck do serviço
 * messaging). Responde 200 depois que a aplicação subiu, o que inclui a
 * conexão com o banco feita na partida.
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}

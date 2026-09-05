package com.temnafesta.notificacao.domain.ports;

public interface MensageriaExternaPort {
    void enviar(String telefoneDestino, String mensagem);
}
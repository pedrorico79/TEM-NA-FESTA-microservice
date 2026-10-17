package com.temnafesta.notificacao.infrastructure.config;

import com.temnafesta.notificacao.application.usecase.ProcessarNotificacaoUseCase;
import com.temnafesta.notificacao.domain.ports.MensageriaExternaPort;
import com.temnafesta.notificacao.domain.ports.NotificacaoRepositoryPort;
import com.temnafesta.notificacao.domain.ports.TemplateMensagemRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {


    @Bean
    public ProcessarNotificacaoUseCase processarNotificacaoUseCase(
            NotificacaoRepositoryPort notificacaoRepository,
            TemplateMensagemRepositoryPort templateRepository,
            MensageriaExternaPort mensageriaExterna) {
        return new ProcessarNotificacaoUseCase(notificacaoRepository, templateRepository, mensageriaExterna);
    }
}

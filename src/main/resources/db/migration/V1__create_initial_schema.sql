-- V1__create_initial_schema.sql
-- Initial schema for Tem Na Festa Notification Service
-- Database: MySQL 8.0+

CREATE DATABASE IF NOT EXISTS `temnafesta_notificacao`
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `temnafesta_notificacao`;

-- -----------------------------------------------------
-- Table: template_mensagem
-- -----------------------------------------------------
CREATE TABLE `template_mensagem` (
    `id` BINARY(16) NOT NULL,
    `tipo_evento` VARCHAR(50) NOT NULL,
    `corpo_texto` TEXT NOT NULL,
    `ativo` BOOLEAN NOT NULL DEFAULT TRUE,
    `data_criacao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_atualizacao` DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_template_tipo_evento_ativo` (`tipo_evento`, `ativo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- Table: notificacao
-- -----------------------------------------------------
CREATE TABLE `notificacao` (
    `id` BINARY(16) NOT NULL,
    `pedido_id` VARCHAR(100) NOT NULL,
    `telefone_destino` VARCHAR(20) NOT NULL,
    `tipo_evento` VARCHAR(50) NOT NULL,
    `data_ocorrencia` DATETIME NOT NULL,
    `status_envio` VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    `conteudo_formatado` TEXT NOT NULL,
    `data_criacao` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `data_processamento` DATETIME NULL,
    `tentativas` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    KEY `idx_notificacao_pedido_tipo` (`pedido_id`, `tipo_evento`),
    KEY `idx_notificacao_status` (`status_envio`),
    KEY `idx_notificacao_data_criacao` (`data_criacao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- -----------------------------------------------------
-- Initial Data: Default Templates
-- -----------------------------------------------------
-- Template for CRIACAO_PEDIDO (Recibo Digital com chave PIX)
INSERT INTO `template_mensagem` (`id`, `tipo_evento`, `corpo_texto`, `ativo`, `data_criacao`)
VALUES (
    UUID_TO_BIN(UUID()),
    'CRIACAO_PEDIDO',
    'Olá {nomeCliente}! Seu pedido #{numeroPedido} foi confirmado. Valor total: R$ {valorTotal}. Para finalizar, realize o pagamento via PIX usando a chave: [CHAVE_PIX_AQUI]. Obrigado pela preferência! - Tem Na Festa',
    TRUE,
    NOW()
);

-- Template for ATUALIZACAO_STATUS (Aviso de status)
INSERT INTO `template_mensagem` (`id`, `tipo_evento`, `corpo_texto`, `ativo`, `data_criacao`)
VALUES (
    UUID_TO_BIN(UUID()),
    'ATUALIZACAO_STATUS',
    'Olá {nomeCliente}! Seu pedido #{numeroPedido} teve o status atualizado. Valor: R$ {valorTotal}.',
    TRUE,
    NOW()
);
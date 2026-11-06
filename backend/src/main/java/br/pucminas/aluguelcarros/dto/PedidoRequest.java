package br.pucminas.aluguelcarros.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

// Dados para criar ou modificar um pedido de aluguel
public record PedidoRequest(
        @NotNull(message = "escolha um automóvel") Long automovelId,
        @NotNull(message = "informe a data de início") LocalDate dataInicio,
        @NotNull(message = "informe a data de fim") LocalDate dataFim,
        String observacao) {
}

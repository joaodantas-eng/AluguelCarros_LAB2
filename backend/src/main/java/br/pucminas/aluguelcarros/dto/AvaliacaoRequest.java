package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Avaliação financeira feita pelo agente: aprovado (parecer positivo) ou não
public record AvaliacaoRequest(
        @NotNull(message = "informe se o pedido foi aprovado") Boolean aprovado,
        @NotBlank(message = "escreva o parecer") String parecer) {
}

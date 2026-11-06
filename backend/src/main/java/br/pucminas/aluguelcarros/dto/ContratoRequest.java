package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.NotNull;

import br.pucminas.aluguelcarros.model.TipoProprietario;

/**
 * Dados para executar o contrato de um pedido aprovado.
 * valorCredito e numeroParcelas são opcionais: só preencha se houver contrato de crédito
 * (e só um agente do tipo BANCO pode conceder crédito).
 */
public record ContratoRequest(
        @NotNull(message = "informe o proprietário do automóvel") TipoProprietario proprietario,
        Double valorCredito,
        Integer numeroParcelas) {
}

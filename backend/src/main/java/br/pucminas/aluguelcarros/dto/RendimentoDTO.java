package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import br.pucminas.aluguelcarros.model.Rendimento;

/**
 * DTO = objeto só para transportar dados entre o front-end e o back-end.
 * "record" é uma classe simples do Java que já vem com construtor e getters.
 */
public record RendimentoDTO(
        @NotBlank(message = "informe a entidade empregadora") String entidadeEmpregadora,
        @NotNull(message = "informe o valor") @PositiveOrZero(message = "não pode ser negativo") Double valor) {

    public static RendimentoDTO de(Rendimento rendimento) {
        return new RendimentoDTO(rendimento.getEntidadeEmpregadora(), rendimento.getValor());
    }

    public Rendimento paraModel() {
        return new Rendimento(entidadeEmpregadora, valor);
    }
}

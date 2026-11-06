package br.pucminas.aluguelcarros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import br.pucminas.aluguelcarros.model.TipoProprietario;

public record AutomovelRequest(
        @NotBlank(message = "informe a matrícula") String matricula,
        @NotNull(message = "informe o ano") Integer ano,
        @NotBlank(message = "informe a marca") String marca,
        @NotBlank(message = "informe o modelo") String modelo,
        @NotBlank(message = "informe a placa") String placa,
        TipoProprietario proprietario) {
}

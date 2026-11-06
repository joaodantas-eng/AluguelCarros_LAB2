package br.pucminas.aluguelcarros.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Dados que o front-end envia para cadastrar ou atualizar um cliente.
 * No cadastro a senha é obrigatória; na atualização ela é opcional
 * (se vier vazia, a senha antiga é mantida).
 */
public record ClienteRequest(
        @NotBlank(message = "informe o login") String login,
        String senha,
        @NotBlank(message = "informe o nome") String nome,
        @NotBlank(message = "informe o RG") String rg,
        @NotBlank(message = "informe o CPF") String cpf,
        @NotBlank(message = "informe o endereço") String endereco,
        String profissao,
        @Valid @Size(max = 3, message = "no máximo 3 rendimentos") List<RendimentoDTO> rendimentos) {
}

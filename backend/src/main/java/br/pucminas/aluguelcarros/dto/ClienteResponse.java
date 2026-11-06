package br.pucminas.aluguelcarros.dto;

import java.util.List;

import br.pucminas.aluguelcarros.model.Cliente;

// Dados do cliente que o back-end devolve (repare que a senha NÃO vai junto)
public record ClienteResponse(
        Long id,
        String login,
        String nome,
        String rg,
        String cpf,
        String endereco,
        String profissao,
        List<RendimentoDTO> rendimentos) {

    public static ClienteResponse de(Cliente cliente) {
        List<RendimentoDTO> rendimentos = cliente.getRendimentos().stream()
                .map(RendimentoDTO::de)
                .toList();

        return new ClienteResponse(cliente.getId(), cliente.getLogin(), cliente.getNome(),
                cliente.getRg(), cliente.getCpf(), cliente.getEndereco(),
                cliente.getProfissao(), rendimentos);
    }
}

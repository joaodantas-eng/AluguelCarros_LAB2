package br.pucminas.aluguelcarros.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.pucminas.aluguelcarros.model.StatusPedido;
import br.pucminas.aluguelcarros.model.TipoProprietario;

/**
 * Tudo o que a tela precisa saber sobre um pedido.
 * Os campos do contrato ficam null enquanto o pedido não for contratado.
 */
public record PedidoResponse(
        Long id,
        Long clienteId,
        String clienteNome,
        Long automovelId,
        String automovel,
        String placa,
        LocalDate dataInicio,
        LocalDate dataFim,
        String observacao,
        StatusPedido status,
        LocalDateTime dataCriacao,
        String agenteAvaliador,
        String parecer,
        Long contratoId,
        TipoProprietario proprietario,
        String bancoCredito,
        Double valorCredito,
        Integer numeroParcelas) {
}

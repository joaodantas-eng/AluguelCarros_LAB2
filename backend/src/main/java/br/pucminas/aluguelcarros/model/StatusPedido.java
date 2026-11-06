package br.pucminas.aluguelcarros.model;

/**
 * Situação de um pedido de aluguel.
 * EM_ANALISE -> APROVADO -> CONTRATADO
 * EM_ANALISE -> REPROVADO
 * EM_ANALISE -> CANCELADO (pelo cliente)
 */
public enum StatusPedido {
    EM_ANALISE,
    APROVADO,
    REPROVADO,
    CANCELADO,
    CONTRATADO
}

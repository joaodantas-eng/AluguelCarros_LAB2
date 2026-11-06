package br.pucminas.aluguelcarros.exception;

// Erro de regra de negócio (ex.: CPF já cadastrado). Vira resposta HTTP 400.
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}

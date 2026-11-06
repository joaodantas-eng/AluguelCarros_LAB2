package br.pucminas.aluguelcarros.exception;

// Quando algo procurado não existe (ex.: pedido com id inválido). Vira resposta HTTP 404.
public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

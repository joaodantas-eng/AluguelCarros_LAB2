package br.pucminas.aluguelcarros.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Transforma as exceções em respostas JSON simples: { "mensagem": "..." }
 * Assim o front-end consegue mostrar o erro para o usuário.
 */
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> regraNegocio(RegraNegocioException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", e.getMessage()));
    }

    @ExceptionHandler(NaoEncontradoException.class)
    public ResponseEntity<Map<String, String>> naoEncontrado(NaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensagem", e.getMessage()));
    }

    // Erros das anotações de validação (@NotBlank, @Size...) dos DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacao(MethodArgumentNotValidException e) {
        String mensagem = "Dados inválidos";
        if (e.getBindingResult().getFieldError() != null) {
            mensagem = e.getBindingResult().getFieldError().getField() + ": "
                    + e.getBindingResult().getFieldError().getDefaultMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensagem", mensagem));
    }
}

package br.pucminas.aluguelcarros.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.ClienteResponse;
import br.pucminas.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;

/**
 * CRUD de cliente.
 * O "Authentication" é preenchido pelo Spring Security com o usuário logado,
 * então authentication.getName() devolve o login de quem fez a requisição.
 */
@RestController
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/api/cliente/perfil")
    public ClienteResponse meuPerfil(Authentication authentication) {
        return clienteService.buscarPerfil(authentication.getName());
    }

    @PutMapping("/api/cliente/perfil")
    public ClienteResponse atualizarPerfil(Authentication authentication,
                                           @Valid @RequestBody ClienteRequest dados) {
        return clienteService.atualizar(authentication.getName(), dados);
    }

    @DeleteMapping("/api/cliente/perfil")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirConta(Authentication authentication) {
        clienteService.excluir(authentication.getName());
    }

    // Agentes podem consultar a lista de clientes
    @GetMapping("/api/agente/clientes")
    public List<ClienteResponse> listarClientes() {
        return clienteService.listarTodos();
    }
}

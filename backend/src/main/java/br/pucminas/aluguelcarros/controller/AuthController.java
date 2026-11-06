package br.pucminas.aluguelcarros.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.aluguelcarros.dto.ClienteRequest;
import br.pucminas.aluguelcarros.dto.ClienteResponse;
import br.pucminas.aluguelcarros.dto.UsuarioLogadoResponse;
import br.pucminas.aluguelcarros.exception.NaoEncontradoException;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;
import br.pucminas.aluguelcarros.service.ClienteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final ClienteService clienteService;
    private final UsuarioRepository usuarioRepository;

    public AuthController(ClienteService clienteService, UsuarioRepository usuarioRepository) {
        this.clienteService = clienteService;
        this.usuarioRepository = usuarioRepository;
    }

    // Cadastro prévio do cliente (rota liberada, sem login)
    @PostMapping("/cadastro")
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse cadastrar(@Valid @RequestBody ClienteRequest dados) {
        return clienteService.cadastrar(dados);
    }

    // Usado pela tela de login: se o login/senha estiverem certos, devolve quem é o usuário
    @GetMapping("/me")
    public UsuarioLogadoResponse usuarioLogado(Authentication authentication) {
        return usuarioRepository.findByLogin(authentication.getName())
                .map(UsuarioLogadoResponse::de)
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado"));
    }
}

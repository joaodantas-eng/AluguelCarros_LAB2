package br.pucminas.aluguelcarros.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.dto.PedidoResponse;
import br.pucminas.aluguelcarros.service.PedidoService;
import jakarta.validation.Valid;

// Pedidos do ponto de vista do CLIENTE (introduzir, consultar, modificar e cancelar)
@RestController
@RequestMapping("/api/cliente/pedidos")
public class PedidoClienteController {

    private final PedidoService pedidoService;

    public PedidoClienteController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoResponse> meusPedidos(Authentication authentication) {
        return pedidoService.listarDoCliente(authentication.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse criar(Authentication authentication, @Valid @RequestBody PedidoRequest dados) {
        return pedidoService.criar(authentication.getName(), dados);
    }

    @PutMapping("/{id}")
    public PedidoResponse modificar(Authentication authentication, @PathVariable Long id,
                                    @Valid @RequestBody PedidoRequest dados) {
        return pedidoService.modificarPeloCliente(authentication.getName(), id, dados);
    }

    @PatchMapping("/{id}/cancelar")
    public PedidoResponse cancelar(Authentication authentication, @PathVariable Long id) {
        return pedidoService.cancelar(authentication.getName(), id);
    }
}

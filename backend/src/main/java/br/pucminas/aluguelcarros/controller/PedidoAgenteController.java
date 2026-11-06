package br.pucminas.aluguelcarros.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.aluguelcarros.dto.AvaliacaoRequest;
import br.pucminas.aluguelcarros.dto.ContratoRequest;
import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.dto.PedidoResponse;
import br.pucminas.aluguelcarros.service.ContratoService;
import br.pucminas.aluguelcarros.service.PedidoService;
import jakarta.validation.Valid;

// Pedidos do ponto de vista do AGENTE (consultar, modificar, avaliar e executar contrato)
@RestController
@RequestMapping("/api/agente/pedidos")
public class PedidoAgenteController {

    private final PedidoService pedidoService;
    private final ContratoService contratoService;

    public PedidoAgenteController(PedidoService pedidoService, ContratoService contratoService) {
        this.pedidoService = pedidoService;
        this.contratoService = contratoService;
    }

    @GetMapping
    public List<PedidoResponse> listarTodos() {
        return pedidoService.listarTodos();
    }

    @PutMapping("/{id}")
    public PedidoResponse modificar(@PathVariable Long id, @Valid @RequestBody PedidoRequest dados) {
        return pedidoService.modificarPeloAgente(id, dados);
    }

    @PatchMapping("/{id}/avaliacao")
    public PedidoResponse avaliar(Authentication authentication, @PathVariable Long id,
                                  @Valid @RequestBody AvaliacaoRequest dados) {
        return pedidoService.avaliar(authentication.getName(), id, dados);
    }

    @PostMapping("/{id}/contrato")
    public PedidoResponse executarContrato(Authentication authentication, @PathVariable Long id,
                                           @Valid @RequestBody ContratoRequest dados) {
        return contratoService.executar(authentication.getName(), id, dados);
    }
}

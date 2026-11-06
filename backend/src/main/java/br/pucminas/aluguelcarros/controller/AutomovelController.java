package br.pucminas.aluguelcarros.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.pucminas.aluguelcarros.dto.AutomovelRequest;
import br.pucminas.aluguelcarros.dto.AutomovelResponse;
import br.pucminas.aluguelcarros.service.AutomovelService;
import jakarta.validation.Valid;

@RestController
public class AutomovelController {

    private final AutomovelService automovelService;

    public AutomovelController(AutomovelService automovelService) {
        this.automovelService = automovelService;
    }

    // Qualquer usuário logado pode ver os automóveis (o cliente precisa escolher um)
    @GetMapping("/api/automoveis")
    public List<AutomovelResponse> listar() {
        return automovelService.listar();
    }

    // Cadastro, edição e exclusão: só agentes
    @PostMapping("/api/agente/automoveis")
    @ResponseStatus(HttpStatus.CREATED)
    public AutomovelResponse criar(@Valid @RequestBody AutomovelRequest dados) {
        return automovelService.criar(dados);
    }

    @PutMapping("/api/agente/automoveis/{id}")
    public AutomovelResponse atualizar(@PathVariable Long id, @Valid @RequestBody AutomovelRequest dados) {
        return automovelService.atualizar(id, dados);
    }

    @DeleteMapping("/api/agente/automoveis/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        automovelService.excluir(id);
    }
}

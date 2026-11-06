package br.pucminas.aluguelcarros.dto;

import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Usuario;

// Informações de quem está logado (o front-end usa o "perfil" para montar o menu)
public record UsuarioLogadoResponse(Long id, String login, String nome, String perfil, String tipoAgente) {

    public static UsuarioLogadoResponse de(Usuario usuario) {
        String tipoAgente = null;
        if (usuario instanceof Agente agente) {
            tipoAgente = agente.getTipo().name();
        }
        return new UsuarioLogadoResponse(usuario.getId(), usuario.getLogin(),
                usuario.getNome(), usuario.getPerfil(), tipoAgente);
    }
}

package br.pucminas.aluguelcarros.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.pucminas.aluguelcarros.model.Usuario;
import br.pucminas.aluguelcarros.repository.UsuarioRepository;

/**
 * O Spring Security chama esta classe no login para buscar o usuário no banco.
 * Depois ele mesmo compara a senha digitada com a senha criptografada.
 */
@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // roles("CLIENTE") vira a permissão "ROLE_CLIENTE", usada no SecurityConfig
        return User.withUsername(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(usuario.getPerfil())
                .build();
    }
}

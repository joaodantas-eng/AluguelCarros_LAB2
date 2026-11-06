package br.pucminas.aluguelcarros.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.aluguelcarros.model.Usuario;

// O Spring Data cria a implementação sozinho a partir do nome dos métodos
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);
    boolean existsByLogin(String login);
}

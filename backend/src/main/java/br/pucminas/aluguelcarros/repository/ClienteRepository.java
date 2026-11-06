package br.pucminas.aluguelcarros.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.aluguelcarros.model.Cliente;

// O Spring Data cria a implementação sozinho a partir do nome dos métodos
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByLogin(String login);
    boolean existsByCpf(String cpf);
}

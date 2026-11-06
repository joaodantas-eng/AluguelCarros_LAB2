package br.pucminas.aluguelcarros.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.aluguelcarros.model.Agente;

// O Spring Data cria a implementação sozinho a partir do nome dos métodos
public interface AgenteRepository extends JpaRepository<Agente, Long> {

    Optional<Agente> findByLogin(String login);
    boolean existsByCnpj(String cnpj);
}

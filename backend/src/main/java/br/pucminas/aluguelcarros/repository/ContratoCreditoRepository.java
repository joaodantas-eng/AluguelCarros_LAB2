package br.pucminas.aluguelcarros.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.aluguelcarros.model.ContratoCredito;

// O Spring Data cria a implementação sozinho a partir do nome dos métodos
public interface ContratoCreditoRepository extends JpaRepository<ContratoCredito, Long> {

    Optional<ContratoCredito> findByContratoId(Long contratoId);
}

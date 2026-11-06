package br.pucminas.aluguelcarros.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.aluguelcarros.model.Contrato;

// O Spring Data cria a implementação sozinho a partir do nome dos métodos
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    Optional<Contrato> findByPedidoId(Long pedidoId);
}

package br.pucminas.aluguelcarros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.aluguelcarros.model.PedidoAluguel;

// O Spring Data cria a implementação sozinho a partir do nome dos métodos
public interface PedidoRepository extends JpaRepository<PedidoAluguel, Long> {

    List<PedidoAluguel> findByClienteIdOrderByIdDesc(Long clienteId);
    List<PedidoAluguel> findAllByOrderByIdDesc();
    boolean existsByAutomovelId(Long automovelId);
}

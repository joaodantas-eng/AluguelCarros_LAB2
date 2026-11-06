package br.pucminas.aluguelcarros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.pucminas.aluguelcarros.model.Automovel;

// O Spring Data cria a implementação sozinho a partir do nome dos métodos
public interface AutomovelRepository extends JpaRepository<Automovel, Long> {

    List<Automovel> findAllByOrderByMarcaAscModeloAsc();
}

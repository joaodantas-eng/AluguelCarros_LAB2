package br.pucminas.aluguelcarros.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.pucminas.aluguelcarros.dto.AutomovelRequest;
import br.pucminas.aluguelcarros.dto.AutomovelResponse;
import br.pucminas.aluguelcarros.exception.NaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Automovel;
import br.pucminas.aluguelcarros.model.TipoProprietario;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;

@Service
public class AutomovelService {

    private final AutomovelRepository automovelRepository;
    private final PedidoRepository pedidoRepository;

    public AutomovelService(AutomovelRepository automovelRepository, PedidoRepository pedidoRepository) {
        this.automovelRepository = automovelRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<AutomovelResponse> listar() {
        return automovelRepository.findAllByOrderByMarcaAscModeloAsc().stream()
                .map(AutomovelResponse::de)
                .toList();
    }

    @Transactional
    public AutomovelResponse criar(AutomovelRequest dados) {
        Automovel automovel = new Automovel();
        preencher(automovel, dados);
        return AutomovelResponse.de(automovelRepository.save(automovel));
    }

    @Transactional
    public AutomovelResponse atualizar(Long id, AutomovelRequest dados) {
        Automovel automovel = buscarPorId(id);
        preencher(automovel, dados);
        return AutomovelResponse.de(automovelRepository.save(automovel));
    }

    @Transactional
    public void excluir(Long id) {
        Automovel automovel = buscarPorId(id);
        if (pedidoRepository.existsByAutomovelId(id)) {
            throw new RegraNegocioException("Este automóvel já tem pedidos e não pode ser excluído");
        }
        automovelRepository.delete(automovel);
    }

    public Automovel buscarPorId(Long id) {
        return automovelRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Automóvel não encontrado"));
    }

    private void preencher(Automovel automovel, AutomovelRequest dados) {
        automovel.setMatricula(dados.matricula());
        automovel.setAno(dados.ano());
        automovel.setMarca(dados.marca());
        automovel.setModelo(dados.modelo());
        automovel.setPlaca(dados.placa().toUpperCase());
        if (dados.proprietario() != null) {
            automovel.setProprietario(dados.proprietario());
        } else if (automovel.getProprietario() == null) {
            automovel.setProprietario(TipoProprietario.EMPRESA);
        }
    }
}

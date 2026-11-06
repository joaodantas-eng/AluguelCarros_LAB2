package br.pucminas.aluguelcarros.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.pucminas.aluguelcarros.dto.AvaliacaoRequest;
import br.pucminas.aluguelcarros.dto.PedidoRequest;
import br.pucminas.aluguelcarros.dto.PedidoResponse;
import br.pucminas.aluguelcarros.exception.NaoEncontradoException;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Cliente;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.StatusPedido;
import br.pucminas.aluguelcarros.model.TipoProprietario;
import br.pucminas.aluguelcarros.repository.AgenteRepository;
import br.pucminas.aluguelcarros.repository.ContratoCreditoRepository;
import br.pucminas.aluguelcarros.repository.ContratoRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;

/**
 * Regras dos pedidos de aluguel.
 *  - Cliente: cria, consulta, modifica e cancela os PRÓPRIOS pedidos (só enquanto EM_ANALISE).
 *  - Agente: consulta todos, modifica e avalia (aprova ou reprova).
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final AgenteRepository agenteRepository;
    private final ContratoRepository contratoRepository;
    private final ContratoCreditoRepository contratoCreditoRepository;
    private final ClienteService clienteService;
    private final AutomovelService automovelService;

    public PedidoService(PedidoRepository pedidoRepository, AgenteRepository agenteRepository,
                         ContratoRepository contratoRepository, ContratoCreditoRepository contratoCreditoRepository,
                         ClienteService clienteService, AutomovelService automovelService) {
        this.pedidoRepository = pedidoRepository;
        this.agenteRepository = agenteRepository;
        this.contratoRepository = contratoRepository;
        this.contratoCreditoRepository = contratoCreditoRepository;
        this.clienteService = clienteService;
        this.automovelService = automovelService;
    }

    // ===================== CLIENTE =====================

    @Transactional
    public PedidoResponse criar(String loginCliente, PedidoRequest dados) {
        Cliente cliente = clienteService.buscarClientePorLogin(loginCliente);

        PedidoAluguel pedido = new PedidoAluguel();
        pedido.setCliente(cliente);
        preencher(pedido, dados);

        return paraResponse(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarDoCliente(String loginCliente) {
        Cliente cliente = clienteService.buscarClientePorLogin(loginCliente);
        return pedidoRepository.findByClienteIdOrderByIdDesc(cliente.getId()).stream()
                .map(this::paraResponse)
                .toList();
    }

    @Transactional
    public PedidoResponse modificarPeloCliente(String loginCliente, Long id, PedidoRequest dados) {
        PedidoAluguel pedido = buscarPedidoDoCliente(loginCliente, id);
        if (!pedido.podeSerAlteradoPeloCliente()) {
            throw new RegraNegocioException("Só é possível modificar pedidos em análise");
        }
        preencher(pedido, dados);
        return paraResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse cancelar(String loginCliente, Long id) {
        PedidoAluguel pedido = buscarPedidoDoCliente(loginCliente, id);
        if (!pedido.podeSerAlteradoPeloCliente()) {
            throw new RegraNegocioException("Só é possível cancelar pedidos em análise");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
        return paraResponse(pedidoRepository.save(pedido));
    }

    // ===================== AGENTE =====================

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAllByOrderByIdDesc().stream()
                .map(this::paraResponse)
                .toList();
    }

    @Transactional
    public PedidoResponse modificarPeloAgente(Long id, PedidoRequest dados) {
        PedidoAluguel pedido = buscarPorId(id);
        if (pedido.getStatus() == StatusPedido.CANCELADO || pedido.getStatus() == StatusPedido.CONTRATADO) {
            throw new RegraNegocioException("Pedidos cancelados ou contratados não podem ser modificados");
        }
        preencher(pedido, dados);
        return paraResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public PedidoResponse avaliar(String loginAgente, Long id, AvaliacaoRequest dados) {
        PedidoAluguel pedido = buscarPorId(id);
        if (pedido.getStatus() != StatusPedido.EM_ANALISE) {
            throw new RegraNegocioException("Só é possível avaliar pedidos em análise");
        }

        Agente agente = buscarAgentePorLogin(loginAgente);
        pedido.setAgenteAvaliador(agente);
        pedido.setParecer(dados.parecer());
        if (dados.aprovado()) {
            pedido.setStatus(StatusPedido.APROVADO);
        } else {
            pedido.setStatus(StatusPedido.REPROVADO);
        }
        return paraResponse(pedidoRepository.save(pedido));
    }

    // ===================== AUXILIARES =====================

    public PedidoAluguel buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Pedido não encontrado"));
    }

    public Agente buscarAgentePorLogin(String login) {
        return agenteRepository.findByLogin(login)
                .orElseThrow(() -> new NaoEncontradoException("Agente não encontrado"));
    }

    // Garante que o cliente só mexe nos pedidos dele
    private PedidoAluguel buscarPedidoDoCliente(String loginCliente, Long id) {
        PedidoAluguel pedido = buscarPorId(id);
        if (!pedido.getCliente().getLogin().equals(loginCliente)) {
            throw new NaoEncontradoException("Pedido não encontrado");
        }
        return pedido;
    }

    private void preencher(PedidoAluguel pedido, PedidoRequest dados) {
        if (dados.dataFim().isBefore(dados.dataInicio())) {
            throw new RegraNegocioException("A data de fim não pode ser antes da data de início");
        }
        pedido.setAutomovel(automovelService.buscarPorId(dados.automovelId()));
        pedido.setDataInicio(dados.dataInicio());
        pedido.setDataFim(dados.dataFim());
        pedido.setObservacao(dados.observacao());
    }

    // Converte a entidade em DTO (inclui os dados do contrato, se existir)
    public PedidoResponse paraResponse(PedidoAluguel pedido) {
        String agenteAvaliador = null;
        if (pedido.getAgenteAvaliador() != null) {
            agenteAvaliador = pedido.getAgenteAvaliador().getNome();
        }

        Long contratoId = null;
        TipoProprietario proprietario = null;
        String bancoCredito = null;
        Double valorCredito = null;
        Integer numeroParcelas = null;

        Optional<Contrato> contrato = contratoRepository.findByPedidoId(pedido.getId());
        if (contrato.isPresent()) {
            contratoId = contrato.get().getId();
            proprietario = contrato.get().getProprietario();

            Optional<ContratoCredito> credito = contratoCreditoRepository.findByContratoId(contratoId);
            if (credito.isPresent()) {
                bancoCredito = credito.get().getBanco().getNome();
                valorCredito = credito.get().getValor();
                numeroParcelas = credito.get().getNumeroParcelas();
            }
        }

        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getAutomovel().getId(),
                pedido.getAutomovel().getMarca() + " " + pedido.getAutomovel().getModelo()
                        + " (" + pedido.getAutomovel().getAno() + ")",
                pedido.getAutomovel().getPlaca(),
                pedido.getDataInicio(),
                pedido.getDataFim(),
                pedido.getObservacao(),
                pedido.getStatus(),
                pedido.getDataCriacao(),
                agenteAvaliador,
                pedido.getParecer(),
                contratoId,
                proprietario,
                bancoCredito,
                valorCredito,
                numeroParcelas);
    }
}

package br.pucminas.aluguelcarros.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.pucminas.aluguelcarros.dto.ContratoRequest;
import br.pucminas.aluguelcarros.dto.PedidoResponse;
import br.pucminas.aluguelcarros.exception.RegraNegocioException;
import br.pucminas.aluguelcarros.model.Agente;
import br.pucminas.aluguelcarros.model.Contrato;
import br.pucminas.aluguelcarros.model.ContratoCredito;
import br.pucminas.aluguelcarros.model.PedidoAluguel;
import br.pucminas.aluguelcarros.model.StatusPedido;
import br.pucminas.aluguelcarros.model.TipoAgente;
import br.pucminas.aluguelcarros.repository.AutomovelRepository;
import br.pucminas.aluguelcarros.repository.ContratoCreditoRepository;
import br.pucminas.aluguelcarros.repository.ContratoRepository;
import br.pucminas.aluguelcarros.repository.PedidoRepository;

/**
 * Execução do contrato: depois que um pedido recebe parecer positivo (APROVADO),
 * o agente pode executar o contrato. Opcionalmente, um BANCO pode conceder crédito.
 */
@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;
    private final ContratoCreditoRepository contratoCreditoRepository;
    private final PedidoRepository pedidoRepository;
    private final AutomovelRepository automovelRepository;
    private final PedidoService pedidoService;

    public ContratoService(ContratoRepository contratoRepository, ContratoCreditoRepository contratoCreditoRepository,
                           PedidoRepository pedidoRepository, AutomovelRepository automovelRepository,
                           PedidoService pedidoService) {
        this.contratoRepository = contratoRepository;
        this.contratoCreditoRepository = contratoCreditoRepository;
        this.pedidoRepository = pedidoRepository;
        this.automovelRepository = automovelRepository;
        this.pedidoService = pedidoService;
    }

    @Transactional
    public PedidoResponse executar(String loginAgente, Long pedidoId, ContratoRequest dados) {
        PedidoAluguel pedido = pedidoService.buscarPorId(pedidoId);
        if (pedido.getStatus() != StatusPedido.APROVADO) {
            throw new RegraNegocioException("Só é possível executar contrato de pedidos aprovados");
        }

        Agente agente = pedidoService.buscarAgentePorLogin(loginAgente);
        boolean temCredito = dados.valorCredito() != null && dados.valorCredito() > 0;

        // Regra: crédito só pode ser concedido por um banco
        if (temCredito && agente.getTipo() != TipoAgente.BANCO) {
            throw new RegraNegocioException("Apenas bancos podem conceder contrato de crédito");
        }
        if (temCredito && (dados.numeroParcelas() == null || dados.numeroParcelas() <= 0)) {
            throw new RegraNegocioException("Informe o número de parcelas do crédito");
        }

        // 1) Cria o contrato de aluguel
        Contrato contrato = new Contrato();
        contrato.setPedido(pedido);
        contrato.setAgente(agente);
        contrato.setProprietario(dados.proprietario());
        contrato = contratoRepository.save(contrato);

        // 2) Se tiver crédito, cria o contrato de crédito ligado a ele
        if (temCredito) {
            ContratoCredito credito = new ContratoCredito();
            credito.setContrato(contrato);
            credito.setBanco(agente);
            credito.setValor(dados.valorCredito());
            credito.setNumeroParcelas(dados.numeroParcelas());
            contratoCreditoRepository.save(credito);
        }

        // 3) O automóvel passa a ser registrado em nome do proprietário definido no contrato
        pedido.getAutomovel().setProprietario(dados.proprietario());
        automovelRepository.save(pedido.getAutomovel());

        // 4) O pedido vira CONTRATADO
        pedido.setStatus(StatusPedido.CONTRATADO);
        pedidoRepository.save(pedido);

        return pedidoService.paraResponse(pedido);
    }
}

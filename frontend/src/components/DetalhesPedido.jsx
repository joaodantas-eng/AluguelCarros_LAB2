import { formatarData, formatarMoeda } from '../utils/formatar'
import StatusBadge from './StatusBadge'

// Parte de cima do cartão de um pedido (usada pelo cliente e pelo agente)
function DetalhesPedido({ pedido, mostrarCliente }) {
  return (
    <>
      <div className="pedido-topo">
        <strong>
          Pedido #{pedido.id} — {pedido.automovel}
        </strong>
        <StatusBadge status={pedido.status} />
      </div>

      <div className="pedido-detalhes">
        {mostrarCliente && <span>Cliente: {pedido.clienteNome}</span>}
        <span>Placa: {pedido.placa}</span>
        <span>
          Período: {formatarData(pedido.dataInicio)} a {formatarData(pedido.dataFim)}
        </span>
        {pedido.observacao && <span>Obs.: {pedido.observacao}</span>}
        {pedido.agenteAvaliador && <span>Avaliado por: {pedido.agenteAvaliador}</span>}
        {pedido.parecer && <span>Parecer: {pedido.parecer}</span>}
        {pedido.contratoId && (
          <span>
            Contrato nº {pedido.contratoId} — carro em nome de: {pedido.proprietario}
          </span>
        )}
        {pedido.valorCredito && (
          <span>
            Crédito: {formatarMoeda(pedido.valorCredito)} em {pedido.numeroParcelas}x ({pedido.bancoCredito})
          </span>
        )}
      </div>
    </>
  )
}

export default DetalhesPedido

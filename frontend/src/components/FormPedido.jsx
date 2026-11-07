import { useState } from 'react'

const PEDIDO_VAZIO = { automovelId: '', dataInicio: '', dataFim: '', observacao: '' }

/**
 * Formulário de pedido de aluguel (escolher carro e datas).
 * Usado pelo cliente (novo pedido / modificar) e pelo agente (modificar).
 */
function FormPedido({ automoveis, valoresIniciais, textoBotao, onSalvar, onCancelar }) {
  const [pedido, setPedido] = useState({ ...PEDIDO_VAZIO, ...valoresIniciais })

  function alterarCampo(campo, valor) {
    setPedido({ ...pedido, [campo]: valor })
  }

  function handleSubmit(evento) {
    evento.preventDefault()
    // Envia só os campos que o back-end espera
    onSalvar({
      automovelId: Number(pedido.automovelId),
      dataInicio: pedido.dataInicio,
      dataFim: pedido.dataFim,
      observacao: pedido.observacao,
    })
  }

  return (
    <form onSubmit={handleSubmit}>
      <div className="grade">
        <label>
          Automóvel
          <select value={pedido.automovelId} onChange={(e) => alterarCampo('automovelId', e.target.value)} required>
            <option value="">Selecione...</option>
            {automoveis.map((a) => (
              <option key={a.id} value={a.id}>
                {a.marca} {a.modelo} ({a.ano}) - placa {a.placa}
              </option>
            ))}
          </select>
        </label>
        <label>
          Data de início
          <input
            type="date"
            value={pedido.dataInicio}
            onChange={(e) => alterarCampo('dataInicio', e.target.value)}
            required
          />
        </label>
        <label>
          Data de fim
          <input type="date" value={pedido.dataFim} onChange={(e) => alterarCampo('dataFim', e.target.value)} required />
        </label>
      </div>

      <label>
        Observação (opcional)
        <textarea rows="2" value={pedido.observacao || ''} onChange={(e) => alterarCampo('observacao', e.target.value)} />
      </label>

      <div className="botoes">
        <button type="submit" className="botao">
          {textoBotao}
        </button>
        {onCancelar && (
          <button type="button" className="botao botao-secundario" onClick={onCancelar}>
            Voltar
          </button>
        )}
      </div>
    </form>
  )
}

export default FormPedido

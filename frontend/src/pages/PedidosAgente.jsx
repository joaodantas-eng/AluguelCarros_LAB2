import { useEffect, useState } from 'react'
import {
  todosPedidos,
  listarAutomoveis,
  avaliarPedido,
  executarContrato,
  modificarPedidoAgente,
} from '../services/api'
import FormPedido from '../components/FormPedido'
import DetalhesPedido from '../components/DetalhesPedido'
import Mensagem from '../components/Mensagem'
import CabecalhoPagina from '../components/CabecalhoPagina'

const FILTROS = [
  ['TODOS', 'Todos'],
  ['EM_ANALISE', 'Em análise'],
  ['APROVADO', 'Aprovados'],
  ['REPROVADO', 'Reprovados'],
  ['CONTRATADO', 'Contratados'],
  ['CANCELADO', 'Cancelados'],
]

// Página do AGENTE: consultar, modificar, avaliar pedidos e executar contratos
function PedidosAgente({ usuario }) {
  const [pedidos, setPedidos] = useState([])
  const [automoveis, setAutomoveis] = useState([])
  const [filtro, setFiltro] = useState('EM_ANALISE')
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  // Qual ação está aberta: { id: 3, tipo: 'avaliar' | 'contrato' | 'modificar' }
  const [acao, setAcao] = useState(null)

  // Campos dos formulários de avaliação e de contrato
  const [parecer, setParecer] = useState('')
  const [proprietario, setProprietario] = useState('CLIENTE')
  const [valorCredito, setValorCredito] = useState('')
  const [numeroParcelas, setNumeroParcelas] = useState('')

  const ehBanco = usuario.tipoAgente === 'BANCO'

  async function carregar() {
    try {
      setPedidos(await todosPedidos())
      setAutomoveis(await listarAutomoveis())
    } catch (e) {
      setErro(e.message)
    }
  }

  useEffect(() => {
    carregar()
  }, [])

  function abrirAcao(id, tipo) {
    setAcao({ id, tipo })
    setParecer('')
    setProprietario('CLIENTE')
    setValorCredito('')
    setNumeroParcelas('')
  }

  // Executa uma chamada à API e mostra a mensagem de sucesso ou de erro
  async function executar(funcao, mensagem) {
    setErro('')
    setSucesso('')
    try {
      await funcao()
      setSucesso(mensagem)
      setAcao(null)
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  function handleAvaliar(id, aprovado) {
    if (!parecer.trim()) {
      setErro('Escreva o parecer antes de avaliar.')
      return
    }
    executar(
      () => avaliarPedido(id, { aprovado, parecer }),
      aprovado ? 'Pedido aprovado (parecer positivo).' : 'Pedido reprovado.',
    )
  }

  function handleContrato(evento, id) {
    evento.preventDefault()
    const dados = {
      proprietario,
      valorCredito: valorCredito ? Number(valorCredito) : null,
      numeroParcelas: numeroParcelas ? Number(numeroParcelas) : null,
    }
    executar(() => executarContrato(id, dados), 'Contrato executado com sucesso!')
  }

  const pedidosFiltrados = filtro === 'TODOS' ? pedidos : pedidos.filter((p) => p.status === filtro)

  return (
    <main className="pagina container">
      <CabecalhoPagina
        titulo="Pedidos de aluguel"
        subtitulo="Analise os pedidos do ponto de vista financeiro. Pedidos aprovados podem ter o contrato executado."
      />

      <Mensagem tipo="erro">{erro}</Mensagem>
      <Mensagem tipo="sucesso">{sucesso}</Mensagem>

      <div className="filtros">
        {FILTROS.map(([valor, texto]) => (
          <button
            key={valor}
            className={'botao botao-pequeno ' + (filtro === valor ? '' : 'botao-secundario')}
            onClick={() => setFiltro(valor)}
          >
            {texto}
          </button>
        ))}
      </div>

      {pedidosFiltrados.length === 0 && <p className="texto-suave">Nenhum pedido aqui.</p>}

      {pedidosFiltrados.map((pedido) => {
        const aberta = acao && acao.id === pedido.id ? acao.tipo : null

        return (
          <div className="cartao cartao-faixa" key={pedido.id}>
            <DetalhesPedido pedido={pedido} mostrarCliente />

            {/* Botões de acordo com o status */}
            {!aberta && (
              <div className="botoes">
                {pedido.status === 'EM_ANALISE' && (
                  <button className="botao botao-pequeno" onClick={() => abrirAcao(pedido.id, 'avaliar')}>
                    Avaliar
                  </button>
                )}
                {pedido.status === 'APROVADO' && (
                  <button className="botao botao-pequeno" onClick={() => abrirAcao(pedido.id, 'contrato')}>
                    Executar contrato
                  </button>
                )}
                {['EM_ANALISE', 'APROVADO', 'REPROVADO'].includes(pedido.status) && (
                  <button
                    className="botao botao-secundario botao-pequeno"
                    onClick={() => abrirAcao(pedido.id, 'modificar')}
                  >
                    Modificar
                  </button>
                )}
              </div>
            )}

            {/* ----- Avaliar ----- */}
            {aberta === 'avaliar' && (
              <div className="caixa-acao">
                <label>
                  Parecer financeiro
                  <textarea rows="3" value={parecer} onChange={(e) => setParecer(e.target.value)} />
                </label>
                <div className="botoes">
                  <button className="botao botao-sucesso" onClick={() => handleAvaliar(pedido.id, true)}>
                    Aprovar
                  </button>
                  <button className="botao botao-perigo" onClick={() => handleAvaliar(pedido.id, false)}>
                    Reprovar
                  </button>
                  <button className="botao botao-secundario" onClick={() => setAcao(null)}>
                    Voltar
                  </button>
                </div>
              </div>
            )}

            {/* ----- Executar contrato ----- */}
            {aberta === 'contrato' && (
              <form className="caixa-acao" onSubmit={(e) => handleContrato(e, pedido.id)}>
                <div className="grade">
                  <label>
                    Automóvel fica em nome de
                    <select value={proprietario} onChange={(e) => setProprietario(e.target.value)}>
                      <option value="CLIENTE">Cliente</option>
                      <option value="EMPRESA">Empresa</option>
                      <option value="BANCO">Banco</option>
                    </select>
                  </label>

                  {/* Só bancos podem conceder crédito */}
                  {ehBanco && (
                    <>
                      <label>
                        Valor do crédito (opcional)
                        <input
                          type="number"
                          min="0"
                          step="0.01"
                          value={valorCredito}
                          onChange={(e) => setValorCredito(e.target.value)}
                        />
                      </label>
                      <label>
                        Nº de parcelas
                        <input
                          type="number"
                          min="1"
                          value={numeroParcelas}
                          onChange={(e) => setNumeroParcelas(e.target.value)}
                          required={valorCredito !== ''}
                        />
                      </label>
                    </>
                  )}
                </div>
                <div className="botoes">
                  <button type="submit" className="botao botao-sucesso">
                    Confirmar contrato
                  </button>
                  <button type="button" className="botao botao-secundario" onClick={() => setAcao(null)}>
                    Voltar
                  </button>
                </div>
              </form>
            )}

            {/* ----- Modificar ----- */}
            {aberta === 'modificar' && (
              <div className="caixa-acao">
                <FormPedido
                  automoveis={automoveis}
                  valoresIniciais={pedido}
                  textoBotao="Salvar alterações"
                  onSalvar={(dados) => executar(() => modificarPedidoAgente(pedido.id, dados), 'Pedido modificado.')}
                  onCancelar={() => setAcao(null)}
                />
              </div>
            )}
          </div>
        )
      })}
    </main>
  )
}

export default PedidosAgente

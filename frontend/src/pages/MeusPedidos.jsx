import { useEffect, useState } from 'react'
import { meusPedidos, criarPedido, modificarMeuPedido, cancelarPedido, listarAutomoveis } from '../services/api'
import FormPedido from '../components/FormPedido'
import DetalhesPedido from '../components/DetalhesPedido'
import Mensagem from '../components/Mensagem'
import CabecalhoPagina from '../components/CabecalhoPagina'

// Página do CLIENTE: introduzir, consultar, modificar e cancelar pedidos
function MeusPedidos() {
  const [pedidos, setPedidos] = useState([])
  const [automoveis, setAutomoveis] = useState([])
  const [editandoId, setEditandoId] = useState(null) // id do pedido que está sendo modificado
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')

  async function carregar() {
    try {
      setPedidos(await meusPedidos())
      setAutomoveis(await listarAutomoveis())
    } catch (e) {
      setErro(e.message)
    }
  }

  useEffect(() => {
    carregar()
  }, [])

  function mostrarMensagem(texto) {
    setErro('')
    setSucesso(texto)
  }

  async function handleCriar(dados) {
    try {
      await criarPedido(dados)
      mostrarMensagem('Pedido enviado! Agora ele será analisado por um agente.')
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  async function handleModificar(id, dados) {
    try {
      await modificarMeuPedido(id, dados)
      setEditandoId(null)
      mostrarMensagem('Pedido modificado.')
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  async function handleCancelar(id) {
    if (!confirm('Deseja mesmo cancelar este pedido?')) return
    try {
      await cancelarPedido(id)
      mostrarMensagem('Pedido cancelado.')
      carregar()
    } catch (e) {
      setErro(e.message)
    }
  }

  return (
    <main className="pagina container">
      <CabecalhoPagina titulo="Meus pedidos" subtitulo="Faça um pedido de aluguel e acompanhe o status." />

      <Mensagem tipo="erro">{erro}</Mensagem>
      <Mensagem tipo="sucesso">{sucesso}</Mensagem>

      <div className="cartao">
        <h3>Novo pedido</h3>
        {/* key muda depois de cada pedido para limpar o formulário */}
        <FormPedido key={pedidos.length} automoveis={automoveis} textoBotao="Enviar pedido" onSalvar={handleCriar} />
      </div>

      <h2 className="titulo-secao">Acompanhar pedidos</h2>
      {pedidos.length === 0 && <p className="texto-suave">Você ainda não fez nenhum pedido.</p>}

      {pedidos.map((pedido) => (
        <div className="cartao cartao-faixa" key={pedido.id}>
          <DetalhesPedido pedido={pedido} />

          {/* Só pode mexer enquanto está em análise */}
          {pedido.status === 'EM_ANALISE' && editandoId !== pedido.id && (
            <div className="botoes">
              <button className="botao botao-secundario botao-pequeno" onClick={() => setEditandoId(pedido.id)}>
                Modificar
              </button>
              <button className="botao botao-perigo botao-pequeno" onClick={() => handleCancelar(pedido.id)}>
                Cancelar pedido
              </button>
            </div>
          )}

          {editandoId === pedido.id && (
            <div className="caixa-acao">
              <FormPedido
                automoveis={automoveis}
                valoresIniciais={pedido}
                textoBotao="Salvar alterações"
                onSalvar={(dados) => handleModificar(pedido.id, dados)}
                onCancelar={() => setEditandoId(null)}
              />
            </div>
          )}
        </div>
      ))}
    </main>
  )
}

export default MeusPedidos

import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { buscarPerfil, atualizarPerfil, excluirConta, sair } from '../services/api'
import FormCliente from '../components/FormCliente'
import Mensagem from '../components/Mensagem'
import CabecalhoPagina from '../components/CabecalhoPagina'

// READ, UPDATE e DELETE do CRUD de cliente (o próprio cliente logado)
function Perfil({ setUsuario }) {
  const [perfil, setPerfil] = useState(null)
  const [erro, setErro] = useState('')
  const [sucesso, setSucesso] = useState('')
  const navigate = useNavigate()

  // Carrega os dados quando a página abre
  useEffect(() => {
    buscarPerfil()
      .then((dados) => setPerfil(dados))
      .catch((e) => setErro(e.message))
  }, [])

  async function salvar(dados) {
    setErro('')
    setSucesso('')
    try {
      const atualizado = await atualizarPerfil(dados)
      setPerfil(atualizado)
      setSucesso('Dados atualizados com sucesso!')
      window.scrollTo(0, 0)

      // Se trocou a senha, precisa entrar de novo (a senha salva no navegador ficou velha)
      if (dados.senha) {
        alert('Senha alterada. Faça login novamente.')
        sair()
        setUsuario(null)
        navigate('/login')
      }
    } catch (e) {
      setErro(e.message)
      window.scrollTo(0, 0)
    }
  }

  async function handleExcluir() {
    if (!confirm('Tem certeza que deseja excluir sua conta? Seus pedidos também serão apagados.')) {
      return
    }
    try {
      await excluirConta()
      sair()
      setUsuario(null)
      navigate('/login')
    } catch (e) {
      setErro(e.message)
      window.scrollTo(0, 0)
    }
  }

  return (
    <main className="pagina container">
      <CabecalhoPagina titulo="Meu perfil" subtitulo="Consulte, altere ou exclua os seus dados." />

      <Mensagem tipo="erro">{erro}</Mensagem>
      <Mensagem tipo="sucesso">{sucesso}</Mensagem>

      {!perfil && !erro && <p className="texto-suave">Carregando...</p>}

      {perfil && (
        <>
          <div className="cartao">
            {/* key força o formulário a recomeçar com os dados novos depois de salvar */}
            <FormCliente
              key={JSON.stringify(perfil)}
              valoresIniciais={perfil}
              modoEdicao={true}
              textoBotao="Salvar alterações"
              onSalvar={salvar}
            />
          </div>

          <div className="cartao">
            <h3>Excluir conta</h3>
            <p className="texto-suave">Só é possível excluir se você não tiver contrato ativo.</p>
            <button className="botao botao-perigo" onClick={handleExcluir}>
              Excluir minha conta
            </button>
          </div>
        </>
      )}
    </main>
  )
}

export default Perfil

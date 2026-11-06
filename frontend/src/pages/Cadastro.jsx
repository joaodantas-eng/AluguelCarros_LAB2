import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { cadastrarCliente } from '../services/api'
import FormCliente from '../components/FormCliente'
import Mensagem from '../components/Mensagem'

// CREATE do CRUD de cliente (cadastro prévio, antes do primeiro login)
function Cadastro() {
  const [erro, setErro] = useState('')
  const navigate = useNavigate()

  async function salvar(dados) {
    setErro('')
    try {
      await cadastrarCliente(dados)
      alert('Cadastro realizado! Agora é só fazer login.')
      navigate('/login')
    } catch (e) {
      setErro(e.message)
      window.scrollTo(0, 0)
    }
  }

  return (
    <div className="pagina-centro">
      <div className="cartao caixa-cadastro">
        <h1>Criar conta de cliente</h1>
        <p className="texto-suave">
          Já tem conta?{' '}
          <Link to="/login" className="link-animado">
            Entrar
          </Link>
        </p>

        <Mensagem tipo="erro">{erro}</Mensagem>

        <FormCliente modoEdicao={false} textoBotao="Cadastrar" onSalvar={salvar} />
      </div>
    </div>
  )
}

export default Cadastro

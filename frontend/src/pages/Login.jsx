import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { entrar } from '../services/api'
import Mensagem from '../components/Mensagem'

function Login({ setUsuario }) {
  const [login, setLogin] = useState('')
  const [senha, setSenha] = useState('')
  const [erro, setErro] = useState('')
  const [carregando, setCarregando] = useState(false)
  const navigate = useNavigate()

  async function handleSubmit(evento) {
    evento.preventDefault() // impede o formulário de recarregar a página
    setErro('')
    setCarregando(true)

    try {
      const usuario = await entrar(login, senha)
      setUsuario(usuario)
      navigate('/')
    } catch (e) {
      setErro(e.message)
    } finally {
      setCarregando(false)
    }
  }

  return (
    <div className="pagina-centro">
      <div className="cartao caixa-login">
        <h1 className="logo-grande">
          Aluga<span>Car</span>
        </h1>
        <p className="texto-suave">Entre com seu usuário e senha para continuar.</p>

        <Mensagem tipo="erro">{erro}</Mensagem>

        <form onSubmit={handleSubmit}>
          <label>
            Login
            <input value={login} onChange={(e) => setLogin(e.target.value)} required autoFocus />
          </label>

          <label>
            Senha
            <input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} required />
          </label>

          <button type="submit" className="botao" disabled={carregando}>
            {carregando ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        <p className="texto-suave centro">
          Ainda não tem conta?{' '}
          <Link to="/cadastro" className="link-animado">
            Cadastre-se
          </Link>
        </p>
      </div>
    </div>
  )
}

export default Login

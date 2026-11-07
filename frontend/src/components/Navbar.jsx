import { NavLink, Link, useNavigate } from 'react-router-dom'
import { sair } from '../services/api'
import Icone from './Icone'

// Links do menu de cada perfil: [endereço, texto, ícone]
const LINKS = {
  CLIENTE: [
    ['/pedidos', 'Meus pedidos', 'pedido'],
    ['/perfil', 'Meu perfil', 'usuario'],
  ],
  AGENTE: [
    ['/agente/pedidos', 'Pedidos', 'pedido'],
    ['/agente/clientes', 'Clientes', 'usuarios'],
  ],
}

// Botão do menu igual ao do portfólio: no hover aparece um círculo com a seta
function BotaoPagina({ para, texto, icone }) {
  return (
    <NavLink to={para} className="botao-pagina">
      <span className="inner">
        <Icone nome={icone} />
        {texto}
      </span>
      <span className="circle" aria-hidden="true">
        <Icone nome="seta" tamanho={18} />
      </span>
    </NavLink>
  )
}

// Barra do topo: os links mudam conforme o perfil (CLIENTE ou AGENTE)
function Navbar({ usuario, setUsuario }) {
  const navigate = useNavigate()

  function handleSair() {
    sair()
    setUsuario(null)
    navigate('/login')
  }

  let tipo = 'Cliente'
  if (usuario.perfil === 'AGENTE') {
    tipo = usuario.tipoAgente === 'BANCO' ? 'Agente (banco)' : 'Agente (empresa)'
  }

  return (
    <header className="menu">
      <div className="container menu-interno">
        <Link to="/" className="logo">
          Aluga<span>Car</span>
        </Link>

        <nav className="menu-links">
          {LINKS[usuario.perfil].map(([para, texto, icone]) => (
            <BotaoPagina key={para} para={para} texto={texto} icone={icone} />
          ))}
        </nav>

        <div className="menu-usuario">
          <span>
            <strong>{usuario.nome}</strong>
            <small>{tipo}</small>
          </span>
          <button className="botao-sair" onClick={handleSair}>
            Sair
          </button>
        </div>
      </div>
    </header>
  )
}

export default Navbar

import { useState } from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import { usuarioSalvo } from './services/api'
import Navbar from './components/Navbar'
import Rodape from './components/Rodape'
import RotaProtegida from './components/RotaProtegida'
import Login from './pages/Login'
import Cadastro from './pages/Cadastro'
import Perfil from './pages/Perfil'
import Clientes from './pages/Clientes'

function App() {
  // Começa com o usuário que ficou salvo no navegador (se já tinha feito login)
  const [usuario, setUsuario] = useState(usuarioSalvo())

  // Página inicial de cada perfil
  function paginaInicial() {
    if (!usuario) return '/login'
    return usuario.perfil === 'AGENTE' ? '/agente/clientes' : '/perfil'
  }

  return (
    <>
      {/* Menu e rodapé só aparecem depois do login */}
      {usuario && <Navbar usuario={usuario} setUsuario={setUsuario} />}

      <Routes>
        <Route path="/" element={<Navigate to={paginaInicial()} replace />} />
        <Route path="/login" element={<Login setUsuario={setUsuario} />} />
        <Route path="/cadastro" element={<Cadastro />} />

        {/* Páginas do CLIENTE */}
        <Route
          path="/perfil"
          element={
            <RotaProtegida usuario={usuario} perfil="CLIENTE">
              <Perfil setUsuario={setUsuario} />
            </RotaProtegida>
          }
        />

        {/* Páginas do AGENTE */}
        <Route
          path="/agente/clientes"
          element={
            <RotaProtegida usuario={usuario} perfil="AGENTE">
              <Clientes />
            </RotaProtegida>
          }
        />

        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>

      {usuario && <Rodape />}
    </>
  )
}

export default App

import { Navigate } from 'react-router-dom'

// Só deixa ver a página se o usuário estiver logado (e com o perfil certo, se for informado)
function RotaProtegida({ usuario, perfil, children }) {
  if (!usuario) {
    return <Navigate to="/login" replace />
  }

  if (perfil && usuario.perfil !== perfil) {
    return <Navigate to="/" replace />
  }

  return children
}

export default RotaProtegida

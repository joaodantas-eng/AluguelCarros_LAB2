// Caixa de aviso: tipo "erro" (vermelha) ou "sucesso" (verde)
function Mensagem({ tipo, children }) {
  if (!children) return null
  return <div className={'mensagem mensagem-' + tipo}>{children}</div>
}

export default Mensagem

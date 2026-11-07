// Mostra o status do pedido com uma cor diferente para cada situação
const TEXTOS = {
  EM_ANALISE: 'Em análise',
  APROVADO: 'Aprovado',
  REPROVADO: 'Reprovado',
  CANCELADO: 'Cancelado',
  CONTRATADO: 'Contratado',
}

function StatusBadge({ status }) {
  return <span className={'status status-' + status}>{TEXTOS[status]}</span>
}

export default StatusBadge

// Funções pequenas para mostrar valores no formato brasileiro

// 2300 -> "R$ 2.300,00"
export function formatarMoeda(valor) {
  return Number(valor || 0).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' })
}

// "2026-11-05" -> "05/11/2026"
export function formatarData(data) {
  return data ? data.split('-').reverse().join('/') : ''
}

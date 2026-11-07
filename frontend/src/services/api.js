// =====================================================================
// Comunicação com o back-end (Spring Boot)
// Todas as chamadas passam pela função "requisicao", que já coloca
// o login no cabeçalho Authorization (HTTP Basic).
// =====================================================================

const URL_BASE = '/api'

// Transforma "login:senha" em Base64 (formato exigido pelo HTTP Basic).
// O TextEncoder garante que acentos funcionem.
function paraBase64(texto) {
  const bytes = new TextEncoder().encode(texto)
  let binario = ''
  bytes.forEach((byte) => {
    binario += String.fromCharCode(byte)
  })
  return btoa(binario)
}

async function requisicao(caminho, metodo = 'GET', corpo = null) {
  const headers = { 'Content-Type': 'application/json' }

  const credenciais = localStorage.getItem('credenciais')
  if (credenciais) {
    headers.Authorization = 'Basic ' + credenciais
  }

  const resposta = await fetch(URL_BASE + caminho, {
    method: metodo,
    headers: headers,
    body: corpo ? JSON.stringify(corpo) : undefined,
  })

  // 401 = não está logado (ou a senha mudou): volta para o login
  if (resposta.status === 401 && credenciais) {
    sair()
    window.location.href = '/login'
    return null
  }

  // 204 = deu certo, mas não tem conteúdo (ex.: depois de excluir)
  if (resposta.status === 204) {
    return null
  }

  const dados = await resposta.json().catch(() => null)

  if (!resposta.ok) {
    const mensagem = dados && dados.mensagem ? dados.mensagem : 'Erro ' + resposta.status
    throw new Error(mensagem)
  }

  return dados
}

// ===================== AUTENTICAÇÃO =====================

export async function entrar(login, senha) {
  const credenciais = paraBase64(login.trim() + ':' + senha)

  let resposta
  try {
    resposta = await fetch(URL_BASE + '/auth/me', {
      headers: { Authorization: 'Basic ' + credenciais },
    })
  } catch {
    throw new Error('Não foi possível falar com o servidor.')
  }

  // 401 = login ou senha errados
  if (resposta.status === 401) {
    throw new Error('Login ou senha inválidos')
  }

  // Qualquer outro erro: normalmente o back-end não está rodando
  if (!resposta.ok) {
    throw new Error('Erro ' + resposta.status + ': o back-end está rodando? (cd backend e mvn spring-boot:run)')
  }

  const usuario = await resposta.json()
  localStorage.setItem('credenciais', credenciais)
  localStorage.setItem('usuario', JSON.stringify(usuario))
  return usuario
}

export function sair() {
  localStorage.removeItem('credenciais')
  localStorage.removeItem('usuario')
}

export function usuarioSalvo() {
  const texto = localStorage.getItem('usuario')
  return texto ? JSON.parse(texto) : null
}

// ===================== CLIENTE (CRUD) =====================

export function cadastrarCliente(dados) {
  return requisicao('/auth/cadastro', 'POST', dados)
}

export function buscarPerfil() {
  return requisicao('/cliente/perfil')
}

export function atualizarPerfil(dados) {
  return requisicao('/cliente/perfil', 'PUT', dados)
}

export function excluirConta() {
  return requisicao('/cliente/perfil', 'DELETE')
}

export function listarClientes() {
  return requisicao('/agente/clientes')
}

// ===================== AUTOMÓVEIS =====================

export function listarAutomoveis() {
  return requisicao('/automoveis')
}

// ===================== PEDIDOS (CLIENTE) =====================

export function meusPedidos() {
  return requisicao('/cliente/pedidos')
}

export function criarPedido(dados) {
  return requisicao('/cliente/pedidos', 'POST', dados)
}

export function modificarMeuPedido(id, dados) {
  return requisicao('/cliente/pedidos/' + id, 'PUT', dados)
}

export function cancelarPedido(id) {
  return requisicao('/cliente/pedidos/' + id + '/cancelar', 'PATCH')
}

// ===================== PEDIDOS (AGENTE) =====================

export function todosPedidos() {
  return requisicao('/agente/pedidos')
}

export function modificarPedidoAgente(id, dados) {
  return requisicao('/agente/pedidos/' + id, 'PUT', dados)
}

export function avaliarPedido(id, dados) {
  return requisicao('/agente/pedidos/' + id + '/avaliacao', 'PATCH', dados)
}

export function executarContrato(id, dados) {
  return requisicao('/agente/pedidos/' + id + '/contrato', 'POST', dados)
}

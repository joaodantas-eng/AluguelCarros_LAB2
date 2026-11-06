import { useState } from 'react'

const CLIENTE_VAZIO = {
  login: '',
  senha: '',
  nome: '',
  rg: '',
  cpf: '',
  endereco: '',
  profissao: '',
  rendimentos: [],
}

const MAX_RENDIMENTOS = 3

/**
 * Formulário de cliente, usado em dois lugares:
 *  - Cadastro (modoEdicao = false): login e senha obrigatórios
 *  - Meu perfil (modoEdicao = true): login travado e senha opcional
 */
function FormCliente({ valoresIniciais, modoEdicao, textoBotao, onSalvar }) {
  const [cliente, setCliente] = useState({ ...CLIENTE_VAZIO, ...valoresIniciais, senha: '' })
  const [salvando, setSalvando] = useState(false)

  // Atualiza um campo simples (nome, cpf, ...)
  function alterarCampo(campo, valor) {
    setCliente({ ...cliente, [campo]: valor })
  }

  // ----- Rendimentos (máximo 3) -----
  function adicionarRendimento() {
    if (cliente.rendimentos.length >= MAX_RENDIMENTOS) return
    const novos = [...cliente.rendimentos, { entidadeEmpregadora: '', valor: '' }]
    setCliente({ ...cliente, rendimentos: novos })
  }

  function alterarRendimento(indice, campo, valor) {
    const novos = cliente.rendimentos.map((r, i) => (i === indice ? { ...r, [campo]: valor } : r))
    setCliente({ ...cliente, rendimentos: novos })
  }

  function removerRendimento(indice) {
    const novos = cliente.rendimentos.filter((_, i) => i !== indice)
    setCliente({ ...cliente, rendimentos: novos })
  }

  async function handleSubmit(evento) {
    evento.preventDefault()
    setSalvando(true)

    // Monta só os campos que o back-end espera.
    // O valor do rendimento vem como texto do input; o back-end espera número.
    const dados = {
      login: cliente.login,
      senha: cliente.senha,
      nome: cliente.nome,
      rg: cliente.rg,
      cpf: cliente.cpf,
      endereco: cliente.endereco,
      profissao: cliente.profissao,
      rendimentos: cliente.rendimentos.map((r) => ({
        entidadeEmpregadora: r.entidadeEmpregadora,
        valor: Number(r.valor),
      })),
    }

    try {
      await onSalvar(dados)
    } finally {
      setSalvando(false)
    }
  }

  return (
    <form onSubmit={handleSubmit}>
      <h3 className="secao-form">Acesso</h3>
      <div className="grade">
        <label>
          Login
          <input
            value={cliente.login}
            onChange={(e) => alterarCampo('login', e.target.value)}
            disabled={modoEdicao}
            required
          />
        </label>
        <label>
          {modoEdicao ? 'Nova senha (opcional)' : 'Senha (mín. 6 caracteres)'}
          <input
            type="password"
            value={cliente.senha}
            onChange={(e) => alterarCampo('senha', e.target.value)}
            placeholder={modoEdicao ? 'Deixe em branco para manter' : ''}
            minLength={6}
            required={!modoEdicao}
          />
        </label>
      </div>

      <h3 className="secao-form">Dados pessoais</h3>
      <div className="grade">
        <label>
          Nome completo
          <input value={cliente.nome} onChange={(e) => alterarCampo('nome', e.target.value)} required />
        </label>
        <label>
          CPF
          <input
            value={cliente.cpf}
            onChange={(e) => alterarCampo('cpf', e.target.value)}
            placeholder="000.000.000-00"
            required
          />
        </label>
        <label>
          RG
          <input value={cliente.rg} onChange={(e) => alterarCampo('rg', e.target.value)} required />
        </label>
        <label>
          Profissão
          <input value={cliente.profissao || ''} onChange={(e) => alterarCampo('profissao', e.target.value)} />
        </label>
      </div>
      <label>
        Endereço
        <input value={cliente.endereco} onChange={(e) => alterarCampo('endereco', e.target.value)} required />
      </label>

      <h3 className="secao-form">
        Rendimentos{' '}
        <small>
          ({cliente.rendimentos.length} de {MAX_RENDIMENTOS})
        </small>
      </h3>

      {cliente.rendimentos.length === 0 && (
        <p className="texto-suave">Nenhum rendimento informado. Você pode adicionar até 3.</p>
      )}

      {cliente.rendimentos.map((rendimento, indice) => (
        <div className="rendimento" key={indice}>
          <label>
            Entidade empregadora
            <input
              value={rendimento.entidadeEmpregadora}
              onChange={(e) => alterarRendimento(indice, 'entidadeEmpregadora', e.target.value)}
              required
            />
          </label>
          <label>
            Valor mensal (R$)
            <input
              type="number"
              min="0"
              step="0.01"
              value={rendimento.valor}
              onChange={(e) => alterarRendimento(indice, 'valor', e.target.value)}
              required
            />
          </label>
          <button type="button" className="botao botao-secundario" onClick={() => removerRendimento(indice)}>
            Remover
          </button>
        </div>
      ))}

      {cliente.rendimentos.length < MAX_RENDIMENTOS && (
        <div>
          <button type="button" className="botao botao-secundario botao-pequeno" onClick={adicionarRendimento}>
            + Adicionar rendimento
          </button>
        </div>
      )}

      <button type="submit" className="botao" disabled={salvando}>
        {salvando ? 'Salvando...' : textoBotao}
      </button>
    </form>
  )
}

export default FormCliente

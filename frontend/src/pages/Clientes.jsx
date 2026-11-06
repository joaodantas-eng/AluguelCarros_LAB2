import { useEffect, useState } from 'react'
import { listarClientes } from '../services/api'
import { formatarMoeda } from '../utils/formatar'
import Mensagem from '../components/Mensagem'
import CabecalhoPagina from '../components/CabecalhoPagina'

// Soma os rendimentos de um cliente
function rendaTotal(cliente) {
  return cliente.rendimentos.reduce((soma, r) => soma + r.valor, 0)
}

// Agentes podem consultar os clientes cadastrados (com rendimentos, para a análise financeira)
function Clientes() {
  const [clientes, setClientes] = useState([])
  const [erro, setErro] = useState('')

  useEffect(() => {
    listarClientes()
      .then((dados) => setClientes(dados))
      .catch((e) => setErro(e.message))
  }, [])

  return (
    <main className="pagina container">
      <CabecalhoPagina titulo="Clientes" subtitulo="Dados e rendimentos dos clientes, usados na análise financeira." />
      <Mensagem tipo="erro">{erro}</Mensagem>

      <div className="cartao tabela-container">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>CPF</th>
              <th>RG</th>
              <th>Profissão</th>
              <th>Endereço</th>
              <th>Rendimentos</th>
              <th>Renda total</th>
            </tr>
          </thead>
          <tbody>
            {clientes.map((cliente) => (
              <tr key={cliente.id}>
                <td>{cliente.nome}</td>
                <td className="sem-quebra">{cliente.cpf}</td>
                <td className="sem-quebra">{cliente.rg}</td>
                <td>{cliente.profissao}</td>
                <td>{cliente.endereco}</td>
                <td>{cliente.rendimentos.map((r) => r.entidadeEmpregadora).join(', ')}</td>
                <td className="sem-quebra">{formatarMoeda(rendaTotal(cliente))}</td>
              </tr>
            ))}
            {clientes.length === 0 && (
              <tr>
                <td colSpan="7" className="texto-suave">
                  Nenhum cliente cadastrado.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </main>
  )
}

export default Clientes

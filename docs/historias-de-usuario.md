# Histórias de Usuário

Sistema de Aluguel de Carros — LAB02 DIAW (PUC Minas)

**Atores:** Cliente (usuário individual) e Agente (empresa ou banco).

---

## Autenticação

### HU01 — Cadastro de cliente
**Como** cliente, **quero** me cadastrar informando meus dados pessoais, **para** poder usar o sistema.

Critérios de aceitação:
- Devo informar login, senha (mínimo 6 caracteres), nome, RG, CPF, endereço e profissão.
- Posso informar até **3 rendimentos** (entidade empregadora + valor).
- O sistema não aceita login ou CPF repetidos.

### HU02 — Login
**Como** usuário (cliente ou agente), **quero** entrar com usuário e senha, **para** acessar as funções do meu perfil.

Critérios de aceitação:
- Sem login, nenhuma página do sistema pode ser acessada.
- Login ou senha errados mostram mensagem de erro.
- Cliente e agente veem menus diferentes.

---

## Cliente

### HU03 — Manter meus dados
**Como** cliente, **quero** consultar, alterar e excluir meus dados, **para** mantê-los atualizados.

Critérios de aceitação:
- Posso alterar todos os dados, menos o login.
- A senha só muda se eu digitar uma nova.
- Não posso excluir a conta se tiver contrato ativo.

### HU04 — Introduzir pedido de aluguel
**Como** cliente, **quero** criar um pedido escolhendo o automóvel e o período, **para** alugar um carro.

Critérios de aceitação:
- Todo pedido novo começa com status **Em análise**.
- A data de fim não pode ser anterior à data de início.

### HU05 — Consultar pedidos
**Como** cliente, **quero** ver meus pedidos e o status de cada um, **para** acompanhar a análise.

Critérios de aceitação:
- Vejo status, parecer do agente e, se houver, dados do contrato e do crédito.

### HU06 — Modificar pedido
**Como** cliente, **quero** alterar carro, datas ou observação de um pedido, **para** corrigir informações.

Critérios de aceitação:
- Só é possível enquanto o pedido estiver **Em análise**.

### HU07 — Cancelar pedido
**Como** cliente, **quero** cancelar um pedido, **para** desistir do aluguel.

Critérios de aceitação:
- Só é possível enquanto o pedido estiver **Em análise**.

---

## Agente (empresa ou banco)

### HU08 — Consultar pedidos
**Como** agente, **quero** ver todos os pedidos filtrando por status, **para** saber o que precisa ser analisado.

### HU09 — Avaliar pedido
**Como** agente, **quero** analisar o pedido do ponto de vista financeiro e dar um parecer, **para** aprová-lo ou reprová-lo.

Critérios de aceitação:
- O parecer é obrigatório.
- Parecer positivo deixa o pedido **Aprovado**; negativo, **Reprovado**.

### HU10 — Modificar pedido
**Como** agente, **quero** modificar um pedido, **para** ajustar carro ou datas durante a negociação.

Critérios de aceitação:
- Pedidos cancelados ou contratados não podem ser modificados.

### HU11 — Executar contrato
**Como** agente, **quero** executar o contrato de um pedido aprovado, **para** formalizar o aluguel.

Critérios de aceitação:
- Só pedidos **Aprovados** podem virar contrato.
- Devo dizer em nome de quem o automóvel fica registrado: cliente, empresa ou banco.
- O pedido passa para **Contratado**.

### HU12 — Conceder crédito
**Como** banco, **quero** associar um contrato de crédito ao aluguel, **para** financiar o cliente.

Critérios de aceitação:
- Apenas agentes do tipo **banco** podem conceder crédito.
- Informo o valor e o número de parcelas.

### HU13 — Manter automóveis
**Como** agente, **quero** cadastrar, editar e excluir automóveis (matrícula, ano, marca, modelo e placa), **para** que os clientes possam escolhê-los.

Critérios de aceitação:
- Automóvel com pedidos não pode ser excluído.

### HU14 — Consultar clientes
**Como** agente, **quero** ver os clientes e seus rendimentos, **para** apoiar a análise financeira.

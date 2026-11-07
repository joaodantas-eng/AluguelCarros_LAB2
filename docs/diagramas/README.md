# Diagramas UML

Todas as versões dos modelos ficam guardadas aqui, como pede o enunciado.
Cada diagrama está em formato `.png`.

| Diagrama | v1 — modelagem inicial | v2 — revisado (alinhado ao código) |
|---|---|---|
| Casos de uso | [v1](v1/casos-de-uso.png) | [v2](v2/casos-de-uso.png) |
| Classes | [v1](v1/classes.png) | [v2](v2/classes.png) |
| Pacotes (visão lógica) | [v1](v1/pacotes.png) | [v2](v2/pacotes.png) |

## O que mudou da v1 para a v2

**Classes**
- `ContratoCredito` ficava ligado direto ao pedido. Na implementação surgiu a classe **`Contrato`**: o agente executa o contrato do pedido aprovado e, *opcionalmente*, o banco associa um `ContratoCredito` a esse contrato.
- O `TipoProprietario` passou a ficar também no `Contrato` (é o contrato que decide se o carro fica em nome do cliente, da empresa ou do banco).
- Novo status **`CONTRATADO`** em `StatusPedido`.
- Novos atributos em `PedidoAluguel`: `observacao` e `dataCriacao`; novo método `podeSerAlteradoPeloCliente()`.
- `Usuario` ganhou o método abstrato `getPerfil()` (usado no login para separar cliente e agente).
- `Rendimento` virou `@Embeddable` (não tem id próprio, sempre pertence a um cliente). O limite de 3 virou a constante `Cliente.MAX_RENDIMENTOS`.

**Pacotes**
- No back-end entraram os pacotes `dto` (dados que entram/saem da API, sem expor a senha), `exception` (erros de regra de negócio) e `config` (segurança e dados iniciais).
- O front-end foi detalhado em `pages`, `components` e `services`.

**Casos de uso**
- Novos: *Executar contrato*, *Manter automóveis* e *Consultar clientes*.
- *Manter automóveis* existe no back-end, mas a tela foi retirada do front-end por causa do prazo de entrega (ver README principal).
- *Conceder crédito* passou a ser uma extensão (`<<extend>>`) de *Executar contrato*.

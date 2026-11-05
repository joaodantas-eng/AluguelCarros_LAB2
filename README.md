# 🚗 AlugaCar — Sistema de Aluguel de Carros

> [!NOTE]
> Sistema web para gestão de aluguéis de automóveis: clientes fazem pedidos pela internet e agentes (empresas e bancos) avaliam esses pedidos do ponto de vista financeiro.

Laboratório 02 da disciplina **Desenvolvimento e Integração de Aplicações Web (DIAW)** — Engenharia de Software, PUC Minas.

## 🚧 Status do Projeto

![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)
![Java](https://img.shields.io/badge/Java-25-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?logo=springboot&logoColor=white)
![React](https://img.shields.io/badge/React-19-61DAFB?logo=react&logoColor=black)
![Vite](https://img.shields.io/badge/Vite-8-646CFF?logo=vite&logoColor=white)
![Supabase](https://img.shields.io/badge/Supabase-PostgreSQL-3FCF8E?logo=supabase&logoColor=white)

## 📝 Sobre o Projeto

Pretende-se desenvolver um sistema para apoio à gestão de aluguéis de automóveis que permita efetuar, cancelar e modificar pedidos através da Internet.

- O sistema só pode ser usado após **cadastro prévio e login**.
- **Clientes** podem introduzir, modificar, consultar e cancelar pedidos de aluguel.
- **Agentes** (empresas e bancos) podem modificar e avaliar pedidos.
- Pedidos com parecer financeiro positivo seguem para a execução do contrato.
- O aluguel pode estar associado a um **contrato de crédito** concedido por um banco.

## ✨ Funcionalidades Planejadas

- Autenticação (login obrigatório)
- CRUD de cliente (com até 3 rendimentos)
- Criação e acompanhamento do status de pedidos de aluguel
- Avaliação de pedidos pelos agentes
- Execução de contratos e contrato de crédito

## 🛠️ Tecnologias

| Camada | Tecnologia |
|---|---|
| Linguagem | Java (JDK 25) |
| Back-end | Spring Boot 4.0.x (arquitetura MVC) |
| Build | Apache Maven |
| Front-end | React + Vite |
| Banco de dados | Supabase (PostgreSQL) |
| Autenticação | Login com usuário e senha |

## 📂 Documentação

- [Histórias de usuário](docs/historias-de-usuario.md)

## 👥 Autor

João Pedro Silva Dantas — [GitHub](https://github.com/joaodantas-eng)

package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 * Classe base de todos os usuários do sistema (Cliente e Agente).
 * Guarda só o que é comum: login, senha e nome.
 *
 * InheritanceType.JOINED: cria a tabela "usuarios" e uma tabela para cada filha
 * ("clientes" e "agentes"), ligadas pelo mesmo id.
 */
@Entity
@Table(name = "usuarios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    // A senha é salva criptografada (BCrypt), nunca em texto puro
    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String nome;

    // Cada filha diz qual é o seu perfil: "CLIENTE" ou "AGENTE"
    public abstract String getPerfil();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}

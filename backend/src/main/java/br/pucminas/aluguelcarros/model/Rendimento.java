package br.pucminas.aluguelcarros.model;

import jakarta.persistence.Embeddable;

/**
 * Um rendimento do cliente: a entidade empregadora e quanto ele recebe dela.
 * @Embeddable = não tem id próprio, sempre pertence a um Cliente.
 */
@Embeddable
public class Rendimento {

    private String entidadeEmpregadora;
    private Double valor;

    public Rendimento() {
    }

    public Rendimento(String entidadeEmpregadora, Double valor) {
        this.entidadeEmpregadora = entidadeEmpregadora;
        this.valor = valor;
    }

    public String getEntidadeEmpregadora() { return entidadeEmpregadora; }
    public void setEntidadeEmpregadora(String entidadeEmpregadora) { this.entidadeEmpregadora = entidadeEmpregadora; }

    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
}

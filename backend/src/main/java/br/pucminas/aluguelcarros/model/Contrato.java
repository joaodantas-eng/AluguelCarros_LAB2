package br.pucminas.aluguelcarros.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Contrato de aluguel: criado quando um agente executa um pedido aprovado.
 */
@Entity
@Table(name = "contratos")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "pedido_id", unique = true)
    private PedidoAluguel pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "agente_id")
    private Agente agente;

    @Column(nullable = false)
    private LocalDate dataAssinatura = LocalDate.now();

    // Em nome de quem o automóvel fica registrado por causa deste contrato
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProprietario proprietario;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PedidoAluguel getPedido() { return pedido; }
    public void setPedido(PedidoAluguel pedido) { this.pedido = pedido; }

    public Agente getAgente() { return agente; }
    public void setAgente(Agente agente) { this.agente = agente; }

    public LocalDate getDataAssinatura() { return dataAssinatura; }
    public void setDataAssinatura(LocalDate dataAssinatura) { this.dataAssinatura = dataAssinatura; }

    public TipoProprietario getProprietario() { return proprietario; }
    public void setProprietario(TipoProprietario proprietario) { this.proprietario = proprietario; }
}

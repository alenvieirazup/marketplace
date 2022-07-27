package br.com.zup.marketplace.model;

import javax.persistence.*;

@Entity
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Pedido pedido;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Pagamento pagamento;

    @Deprecated
    public Venda() {
    }

    public Venda(Pedido pedido, Pagamento pagamento) {
        this.pedido = pedido;
        this.pagamento = pagamento;
    }

    public Long getId() {
        return id;
    }

}

package br.com.zup.marketplace.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    private Long usuarioId;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name="pedido_id")
    private List<Item> itens;

    @Deprecated
    public Pedido() {
    }

    public Pedido(Long usuarioId, List<Item> produtos) {
        this.usuarioId = usuarioId;
        this.itens = produtos;
    }

    public String getId() {
        return id.toString();
    }

}

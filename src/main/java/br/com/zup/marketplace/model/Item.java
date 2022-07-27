package br.com.zup.marketplace.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long produtoId;
    private String nome;
    private Integer quantidade;
    private BigDecimal preco;

    @Deprecated
    public Item() {
    }

    public Item(Long produtoId, String nome, Integer quantidade, BigDecimal preco) {
        this.produtoId = produtoId;
        this.nome = nome;
        this.quantidade = quantidade;
        this.preco = preco;
    }
}

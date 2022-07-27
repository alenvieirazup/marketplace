package br.com.zup.marketplace.controller.response;

import br.com.zup.marketplace.model.Item;

import java.math.BigDecimal;

public class ItemDaVendaReponse {
    private Long id;
    private String nome;
    private Integer quantidade;
    private BigDecimal preco;

    public ItemDaVendaReponse(ProdutoDaVendaResponse produtoDaVenda, Integer quantidade) {
        this.id = produtoDaVenda.getId();
        this.nome = produtoDaVenda.getNome();
        this.quantidade = quantidade;
        this.preco = produtoDaVenda.getPreco();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public Item toModel() {
        return new Item(id, nome, quantidade, preco);
    }

}

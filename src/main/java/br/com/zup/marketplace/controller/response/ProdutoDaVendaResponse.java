package br.com.zup.marketplace.controller.response;

import java.math.BigDecimal;

public class ProdutoDaVendaResponse {
    private Long id;
    private String nome;
    private BigDecimal preco;

    public ProdutoDaVendaResponse(Long id, String nome, BigDecimal preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

}

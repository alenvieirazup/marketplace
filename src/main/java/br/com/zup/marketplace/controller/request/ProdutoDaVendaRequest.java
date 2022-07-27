package br.com.zup.marketplace.controller.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ProdutoDaVendaRequest {
    @NotNull
    private Long id;
    @NotNull
    @Positive
    private Integer quantidade;

    public ProdutoDaVendaRequest(Long id, Integer quantidade) {
        this.id = id;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

}

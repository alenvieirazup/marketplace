package br.com.zup.marketplace.controller.response;

import java.util.UUID;

public class PagamentoDaVendaResponse {
    private String id;
    private String forma = "Cartao de Credito";
    private String status = "APROVADO";

    public PagamentoDaVendaResponse(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getForma() {
        return forma;
    }

    public String getStatus() {
        return status;
    }

}

package br.com.zup.marketplace.controller.response;

import br.com.zup.marketplace.model.Pagamento;

import java.util.UUID;

public class DadosPagamentoDaVendaResponse {
    private String id;
    private String status;

    public DadosPagamentoDaVendaResponse(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Pagamento toModel(String forma) {
        return new Pagamento(UUID.fromString(id), forma, status);
    }

}

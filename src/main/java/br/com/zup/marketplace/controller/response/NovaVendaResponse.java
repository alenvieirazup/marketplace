package br.com.zup.marketplace.controller.response;

import br.com.zup.marketplace.controller.response.PagamentoDaVendaResponse;
import br.com.zup.marketplace.controller.response.ItemDaVendaReponse;
import br.com.zup.marketplace.controller.response.CompradorDaVendaResponse;

import java.util.List;

public class NovaVendaResponse {
    private String codigoPedido;
    private CompradorDaVendaResponse comprador;
    private List<ItemDaVendaReponse> itens;
    private PagamentoDaVendaResponse pagamento;

    public NovaVendaResponse(String codigoPedido, CompradorDaVendaResponse comprador, List<ItemDaVendaReponse> itens, PagamentoDaVendaResponse pagamento) {
        this.codigoPedido = codigoPedido;
        this.comprador = comprador;
        this.itens = itens;
        this.pagamento = pagamento;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public CompradorDaVendaResponse getComprador() {
        return comprador;
    }

    public List<ItemDaVendaReponse> getItens() {
        return itens;
    }

    public PagamentoDaVendaResponse getPagamento() {
        return pagamento;
    }


}

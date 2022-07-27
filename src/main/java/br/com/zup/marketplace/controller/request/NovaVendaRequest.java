package br.com.zup.marketplace.controller.request;

import br.com.zup.marketplace.controller.request.ProdutoDaVendaRequest;
import br.com.zup.marketplace.controller.request.DadosDoPagamentoDaVendaRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NovaVendaRequest {
    @NotNull
    private Long usuario;
    @Valid
    @NotEmpty
    private List<ProdutoDaVendaRequest> produtos;
    @Valid
    @NotNull
    private DadosDoPagamentoDaVendaRequest pagamento;

    public NovaVendaRequest(Long usuario, List<ProdutoDaVendaRequest> produtos, DadosDoPagamentoDaVendaRequest pagamento) {
        this.usuario = usuario;
        this.produtos = produtos;
        this.pagamento = pagamento;
    }

    public Long getUsuario() {
        return usuario;
    }

    public List<ProdutoDaVendaRequest> getProdutos() {
        return produtos;
    }

    public DadosDoPagamentoDaVendaRequest getPagamento() {
        return pagamento;
    }

}

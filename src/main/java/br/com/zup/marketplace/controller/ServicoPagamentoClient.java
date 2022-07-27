package br.com.zup.marketplace.controller;

import br.com.zup.marketplace.controller.response.DadosPagamentoDaVendaResponse;
import br.com.zup.marketplace.controller.request.DadosPagamentoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pagamentoClient", url = "http://localhost:8081/")
public interface ServicoPagamentoClient {

    @PostMapping("/pagamentos/credito")
    public DadosPagamentoDaVendaResponse pagarNoCredito(@RequestBody DadosPagamentoRequest request);

}

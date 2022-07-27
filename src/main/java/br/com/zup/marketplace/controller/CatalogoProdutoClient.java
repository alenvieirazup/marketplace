package br.com.zup.marketplace.controller;

import br.com.zup.marketplace.controller.response.ProdutoDaVendaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "produtoClient", url = "http://localhost:8082/")
public interface CatalogoProdutoClient {

    @GetMapping("/produtos/{id}")
    public Optional<ProdutoDaVendaResponse> buscarProdutoPor(@PathVariable Long id);

}

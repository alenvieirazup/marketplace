package br.com.zup.marketplace.controller;

import br.com.zup.marketplace.controller.response.UsuarioDaVendaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "usuarioClient", url = "http://localhost:9090/")
public interface GerenciadorUsuarioClient {

    @GetMapping("/usuarios/{id}")
    public Optional<UsuarioDaVendaResponse> buscarUsuarioPor(@PathVariable Long id);

}

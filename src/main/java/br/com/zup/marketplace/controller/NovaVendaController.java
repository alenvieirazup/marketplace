package br.com.zup.marketplace.controller;

import br.com.zup.marketplace.controller.request.DadosPagamentoRequest;
import br.com.zup.marketplace.controller.response.DadosPagamentoDaVendaResponse;
import br.com.zup.marketplace.controller.response.PagamentoDaVendaResponse;
import br.com.zup.marketplace.model.Pagamento;
import br.com.zup.marketplace.model.Pedido;
import br.com.zup.marketplace.model.Venda;
import br.com.zup.marketplace.model.Item;
import br.com.zup.marketplace.controller.response.ItemDaVendaReponse;
import br.com.zup.marketplace.controller.response.ProdutoDaVendaResponse;
import br.com.zup.marketplace.controller.response.CompradorDaVendaResponse;
import br.com.zup.marketplace.controller.response.UsuarioDaVendaResponse;
import br.com.zup.marketplace.controller.request.NovaVendaRequest;
import br.com.zup.marketplace.controller.response.NovaVendaResponse;
import br.com.zup.marketplace.repository.VendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestController
public class NovaVendaController {
    private final GerenciadorUsuarioClient usuarioClient;
    private final CatalogoProdutoClient produtoClient;
    private final ServicoPagamentoClient pagamentoClient;
    private final TransactionTemplate transactionTemplate;
    private final VendaRepository repository;
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;

    public NovaVendaController(GerenciadorUsuarioClient usuarioClient, CatalogoProdutoClient produtoClient,
                               ServicoPagamentoClient pagamentoClient, TransactionTemplate transactionTemplate, VendaRepository repository,
                               KafkaTemplate kafkaTemplate, ObjectMapper objectMapper) {
        this.usuarioClient = usuarioClient;
        this.produtoClient = produtoClient;
        this.pagamentoClient = pagamentoClient;
        this.transactionTemplate = transactionTemplate;
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/vendas")
    public ResponseEntity<?> comprar(@RequestBody @Valid NovaVendaRequest request) {

        UsuarioDaVendaResponse usuarioDaVenda = usuarioClient.buscarUsuarioPor(request.getUsuario())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Seu usuário não foi encontrado"));

        List<BigDecimal> valoresDosItens = new ArrayList<BigDecimal>();
        List<ItemDaVendaReponse> itensDaVenda = new ArrayList<ItemDaVendaReponse>();

        request.getProdutos().forEach((produto) -> {
            ProdutoDaVendaResponse produtoDaVenda = produtoClient.buscarProdutoPor(produto.getId())
                    .orElseThrow(() -> new ResponseStatusException(NOT_FOUND,
                            "Um produto escolhido não foi encontrado"));
            itensDaVenda.add(new ItemDaVendaReponse(produtoDaVenda, produto.getQuantidade()));
            valoresDosItens.add(produtoDaVenda.getPreco().multiply(new BigDecimal(produto.getQuantidade())));
        });

        BigDecimal valorTotal = valoresDosItens.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DadosPagamentoRequest pagamentoParaAprovacao = new DadosPagamentoRequest(request.getPagamento(), valorTotal);
        DadosPagamentoDaVendaResponse pagamentoProcessado = pagamentoClient.pagarNoCredito(pagamentoParaAprovacao);

        Pagamento novoPagamento = pagamentoProcessado.toModel("Cartao");
        List<Item> novosItens = itensDaVenda.stream()
                .map(i -> i.toModel())
                .collect(Collectors.toList());
        Pedido pedido = new Pedido(request.getUsuario(), novosItens);
        Venda venda = new Venda(pedido, novoPagamento);

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                repository.save(venda);

                NovaVendaResponse vendaComPagamento = new NovaVendaResponse(pedido.getId(),
                        new CompradorDaVendaResponse(usuarioDaVenda),
                        itensDaVenda,
                        new PagamentoDaVendaResponse(pagamentoProcessado.getId()));

                if (pagamentoProcessado.getStatus().equals("APROVADO")) {
                    kafkaTemplate.send("teste", vendaComPagamento);
                }

            }
        });
        if (pagamentoProcessado.getStatus().equals("REPROVADO")) {
            throw new ResponseStatusException(UNPROCESSABLE_ENTITY, "Seu pagamento não foi aprovado");
        }

        return ResponseEntity.accepted().build();
    }

}

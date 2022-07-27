package br.com.zup.marketplace;

import br.com.zup.marketplace.controller.CatalogoProdutoClient;
import br.com.zup.marketplace.controller.GerenciadorUsuarioClient;
import br.com.zup.marketplace.controller.ServicoPagamentoClient;
import br.com.zup.marketplace.controller.request.DadosDoPagamentoDaVendaRequest;
import br.com.zup.marketplace.controller.request.NovaVendaRequest;
import br.com.zup.marketplace.controller.request.ProdutoDaVendaRequest;
import br.com.zup.marketplace.controller.response.DadosPagamentoDaVendaResponse;
import br.com.zup.marketplace.controller.response.ProdutoDaVendaResponse;
import br.com.zup.marketplace.controller.response.UsuarioDaVendaResponse;
import br.com.zup.marketplace.exception.ErroPadronizado;
import br.com.zup.marketplace.model.Venda;
import br.com.zup.marketplace.repository.VendaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NovaVendaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private VendaRepository repository;
    @MockBean
    private CatalogoProdutoClient produtoClient;
    @MockBean
    private GerenciadorUsuarioClient usuarioClient;
    @MockBean
    private ServicoPagamentoClient pagamentoClient;
    @MockBean
    private KafkaTemplate kafkaTemplate;

    @BeforeEach
    void setUp() {
        this.repository.deleteAll();
    }

    @Test
    public void naoDeveriaAceitarCompraComDadosNulos() throws Exception {
        NovaVendaRequest vendaRequest = new NovaVendaRequest(null, null, null);

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErroPadronizado erroPadronizado = mapper.readValue(payloadResponse, ErroPadronizado.class);

        assertThat(erroPadronizado.getMensagens())
                .hasSize(3)
                .contains("usuario: não deve ser nulo",
                        "pagamento: não deve ser nulo",
                        "produtos: não deve estar vazio");

    }

    @Test
    public void naoDeveriaAceitarCompraComDadosDeProdutosNulos() throws Exception {
        DadosDoPagamentoDaVendaRequest pagamentoRequest = new DadosDoPagamentoDaVendaRequest(
                "Fulano Silva", "5478659832154582",
                YearMonth.of(2025, 8), "429");
        ProdutoDaVendaRequest produtoRequest = new ProdutoDaVendaRequest(null, null);
        NovaVendaRequest vendaRequest = new NovaVendaRequest(1L, List.of(produtoRequest), pagamentoRequest);

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErroPadronizado erroPadronizado = mapper.readValue(payloadResponse, ErroPadronizado.class);

        assertThat(erroPadronizado.getMensagens())
                .hasSize(2)
                .contains("produtos[0].id: não deve ser nulo",
                        "produtos[0].quantidade: não deve ser nulo");

    }

    @Test
    public void naoDeveriaAceitarCompraComDadosDePagamentoNulos() throws Exception {
        DadosDoPagamentoDaVendaRequest pagamentoRequest = new DadosDoPagamentoDaVendaRequest(
                null, null, null, null);
        ProdutoDaVendaRequest produtoRequest = new ProdutoDaVendaRequest(1L, 1);
        NovaVendaRequest vendaRequest = new NovaVendaRequest(1L, List.of(produtoRequest), pagamentoRequest);

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErroPadronizado erroPadronizado = mapper.readValue(payloadResponse, ErroPadronizado.class);

        assertThat(erroPadronizado.getMensagens())
                .hasSize(4)
                .contains("pagamento.validoAte: não deve ser nulo",
                        "pagamento.numero: não deve estar vazio",
                        "pagamento.codigoSeguranca: não deve estar vazio",
                        "pagamento.titular: não deve estar vazio");

    }

    @Test
    public void naoDeveriaAceitarCompraComDadosInvalidos() throws Exception {
        ProdutoDaVendaRequest produtoRequest = new ProdutoDaVendaRequest(1L, -1);
        DadosDoPagamentoDaVendaRequest pagamentoRequest = new DadosDoPagamentoDaVendaRequest(
                "Fulano Silva", "547865983215458",
                YearMonth.of(1025, 8), "42");
        NovaVendaRequest vendaRequest = new NovaVendaRequest(1L, List.of(produtoRequest), pagamentoRequest);

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isBadRequest()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        ErroPadronizado erroPadronizado = mapper.readValue(payloadResponse, ErroPadronizado.class);

        assertThat(erroPadronizado.getMensagens())
                .hasSize(4)
                .contains("pagamento.validoAte: deve ser uma data no presente ou no futuro",
                        "pagamento.numero: tamanho deve ser entre 16 e 16",
                        "pagamento.codigoSeguranca: tamanho deve ser entre 3 e 3",
                        "produtos[0].quantidade: deve ser maior que 0");

    }

    @Test
    public void deveriaAceitarCompraComDadosValidosEPagamentoAprovado() throws Exception {
        ProdutoDaVendaRequest produtoRequest = new ProdutoDaVendaRequest(1L, 1);
        DadosDoPagamentoDaVendaRequest pagamentoRequest = new DadosDoPagamentoDaVendaRequest(
                "Fulano Silva", "5478659832154582",
                YearMonth.of(2025, 8), "429");
        NovaVendaRequest vendaRequest = new NovaVendaRequest(1L, List.of(produtoRequest), pagamentoRequest);

        UsuarioDaVendaResponse usuario = new UsuarioDaVendaResponse("Fulano Silva",
                "09581234039", "fulano@silva.net", "Rua X",
                LocalDate.of(2000, 02, 01));

        DadosPagamentoDaVendaResponse pagamento = new DadosPagamentoDaVendaResponse(
                "d587d230-6d1e-41c9-84e1-79570eeec2db", "APROVADO");
        ProdutoDaVendaResponse produto = new ProdutoDaVendaResponse(1L, "Placa", BigDecimal.TEN);

        when(usuarioClient.buscarUsuarioPor(any())).thenReturn(Optional.of(usuario));
        when(produtoClient.buscarProdutoPor(any())).thenReturn(Optional.of(produto));
        when(pagamentoClient.pagarNoCredito(any())).thenReturn(pagamento);

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isAccepted()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        List<Venda> vendas = repository.findAll();
        assertEquals(1, vendas.size());
    }

    @Test
    public void naoDeveriaAceitarCompraComPagamentoRecusado() throws Exception {
        ProdutoDaVendaRequest produtoRequest = new ProdutoDaVendaRequest(1L, 1);
        DadosDoPagamentoDaVendaRequest pagamentoRequest = new DadosDoPagamentoDaVendaRequest(
                "Fulano Silva", "5478659832154582",
                YearMonth.of(2025, 8), "429");
        NovaVendaRequest vendaRequest = new NovaVendaRequest(1L, List.of(produtoRequest), pagamentoRequest);

        UsuarioDaVendaResponse usuario = new UsuarioDaVendaResponse("Fulano Silva",
                "09581234039", "fulano@silva.net", "Rua X",
                LocalDate.of(2000, 02, 01));

        DadosPagamentoDaVendaResponse pagamento = new DadosPagamentoDaVendaResponse(
                "d587d230-6d1e-41c9-84e1-79570eeec2db", "REPROVADO");
        ProdutoDaVendaResponse produto = new ProdutoDaVendaResponse(1L, "Placa", BigDecimal.TEN);

        when(usuarioClient.buscarUsuarioPor(any())).thenReturn(Optional.of(usuario));
        when(produtoClient.buscarProdutoPor(any())).thenReturn(Optional.of(produto));
        when(pagamentoClient.pagarNoCredito(any())).thenReturn(pagamento);

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isUnprocessableEntity()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        System.out.println(payloadResponse);
        List<Venda> vendas = repository.findAll();
        assertEquals(1, vendas.size());
    }

    @Test
    public void naoDeveriaAceitarCompraComDadosValidosParaUsuarioInexistente() throws Exception {
        ProdutoDaVendaRequest produtoRequest = new ProdutoDaVendaRequest(1L, 1);
        DadosDoPagamentoDaVendaRequest pagamentoRequest = new DadosDoPagamentoDaVendaRequest(
                "Fulano Silva", "5478659832154582",
                YearMonth.of(2025, 8), "429");
        NovaVendaRequest vendaRequest = new NovaVendaRequest(1L, List.of(produtoRequest), pagamentoRequest);


        when(usuarioClient.buscarUsuarioPor(any())).thenReturn(Optional.empty());

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isNotFound()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

    }

    @Test
    public void naoDeveriaAceitarCompraComDadosValidosParaProdutosInexistente() throws Exception {
        ProdutoDaVendaRequest produtoRequest = new ProdutoDaVendaRequest(1L, 1);
        DadosDoPagamentoDaVendaRequest pagamentoRequest = new DadosDoPagamentoDaVendaRequest(
                "Fulano Silva", "5478659832154582",
                YearMonth.of(2025, 8), "429");
        NovaVendaRequest vendaRequest = new NovaVendaRequest(1L, List.of(produtoRequest), pagamentoRequest);

        UsuarioDaVendaResponse usuario = new UsuarioDaVendaResponse("Fulano Silva",
                "09581234039", "fulano@silva.net", "Rua X",
                LocalDate.of(2000, 02, 01));

        DadosPagamentoDaVendaResponse pagamento = new DadosPagamentoDaVendaResponse(
                "d587d230-6d1e-41c9-84e1-79570eeec2db", "APROVADO");
        ProdutoDaVendaResponse produto = new ProdutoDaVendaResponse(1L, "Placa", BigDecimal.TEN);

        when(usuarioClient.buscarUsuarioPor(any())).thenReturn(Optional.of(usuario));
        when(produtoClient.buscarProdutoPor(any())).thenReturn(Optional.of(produto));
        when(pagamentoClient.pagarNoCredito(any())).thenReturn(pagamento);

        String payloadRequest = mapper.writeValueAsString(vendaRequest);

        MockHttpServletRequestBuilder request = post("/vendas")
                .header("Accept-Language", "pt-br")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadRequest);


        String payloadResponse = mockMvc.perform(request).
                andExpect(
                        status().isAccepted()
                )
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        List<Venda> vendas = repository.findAll();
        assertEquals(1, vendas.size());
    }

}

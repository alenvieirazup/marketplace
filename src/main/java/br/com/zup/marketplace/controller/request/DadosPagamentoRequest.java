package br.com.zup.marketplace.controller.request;

import java.math.BigDecimal;
import java.time.YearMonth;

public class DadosPagamentoRequest {
    private String titular;
    private String numero;
    private String codigoSeguranca;
    private BigDecimal valor;
    private YearMonth validoAte;

    public DadosPagamentoRequest(DadosDoPagamentoDaVendaRequest dadosDoPagamentoDaVendaRequest, BigDecimal valor) {
        this.titular = dadosDoPagamentoDaVendaRequest.getTitular();
        this.numero = dadosDoPagamentoDaVendaRequest.getNumero();
        this.codigoSeguranca = dadosDoPagamentoDaVendaRequest.getCodigoSeguranca();
        this.valor = valor;
        this.validoAte = dadosDoPagamentoDaVendaRequest.getValidoAte();
    }

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public YearMonth getValidoAte() {
        return validoAte;
    }

}

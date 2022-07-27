package br.com.zup.marketplace.controller.request;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.YearMonth;

public class DadosDoPagamentoDaVendaRequest {
    @NotEmpty
    private String titular;
    @NotEmpty
    @Size(min = 16, max = 16)
    private String numero;
    @NotNull
    @FutureOrPresent
    private YearMonth validoAte;
    @NotEmpty
    @Size(min = 3, max = 3)
    private String codigoSeguranca;

    public DadosDoPagamentoDaVendaRequest(String titular, String numero, YearMonth validoAte, String codigoSeguranca) {
        this.titular = titular;
        this.numero = numero;
        this.validoAte = validoAte;
        this.codigoSeguranca = codigoSeguranca;
    }

    public String getTitular() {
        return titular;
    }

    public String getNumero() {
        return numero;
    }

    public YearMonth getValidoAte() {
        return validoAte;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

}

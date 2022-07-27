package br.com.zup.marketplace.controller.response;

import java.time.LocalDate;

public class CompradorDaVendaResponse {
    private String nome;
    private String cpf;
    private String email;
    private String endereco;
    private LocalDate dataNascimento;

    public CompradorDaVendaResponse(UsuarioDaVendaResponse usuario) {
        this.nome = usuario.getNome();
        this.cpf = usuario.getCpf();
        this.email = usuario.getEmail();
        this.endereco = usuario.getEndereco();
        this.dataNascimento = usuario.getDataNascimento();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}
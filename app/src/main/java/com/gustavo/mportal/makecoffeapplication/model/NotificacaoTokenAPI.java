package com.gustavo.mportal.makecoffeapplication.model;

public class NotificacaoTokenAPI {
    private String nome;
    private String token_id;

    public NotificacaoTokenAPI(){

    }

    public NotificacaoTokenAPI(String nome, String token_id) {
        this.nome = nome;
        this.token_id = token_id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }
}

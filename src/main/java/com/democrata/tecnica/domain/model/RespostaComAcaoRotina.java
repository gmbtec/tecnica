package com.democrata.tecnica.domain.model;

public class RespostaComAcaoRotina {
    private String descricao;
    private String acao;
    private String rotina;

    public RespostaComAcaoRotina(String descricao, String acao, String rotina) {
        this.descricao = descricao;
        this.acao = acao;
        this.rotina = rotina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAcao() {
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getRotina() {
        return rotina;
    }

    public void setRotina(String rotina) {
        this.rotina = rotina;
    }
// Getters e setters
}

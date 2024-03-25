package com.democrata.tecnica.domain.model;

import jakarta.persistence.*;

@Entity(name = "tb_associacao")
public class AssociacaoPalavras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String palavra;

    private String associada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public String getAssociada() {
        return associada;
    }

    public void setAssociada(String associada) {
        this.associada = associada;
    }
}

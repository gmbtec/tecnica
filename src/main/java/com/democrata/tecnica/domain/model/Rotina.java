package com.democrata.tecnica.domain.model;

import jakarta.persistence.*;
@Entity(name = "tb_rotina")
public class Rotina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String executarRotina;


    private String status;

    @Column
    private String link;

    @ManyToOne
    @JoinColumn(name = "acao_id")
    private Acao acao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutarRotina() {
        return executarRotina;
    }

    public void setExecutarRotina(String executarRotina) {
        this.executarRotina = executarRotina;
    }

    public String getStatus() {
        return status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }
}

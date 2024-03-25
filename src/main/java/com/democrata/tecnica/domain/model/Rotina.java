package com.democrata.tecnica.domain.model;

import jakarta.persistence.*;
@Entity(name = "tb_rotina")
public class Rotina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String executarRotina;

    // Novo campo status com valor padrão "A"

    private String status;

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

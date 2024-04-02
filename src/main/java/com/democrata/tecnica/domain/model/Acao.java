package com.democrata.tecnica.domain.model;

import jakarta.persistence.*;
import java.util.List;
@Entity(name = "tb_acao")
public class Acao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mantenha ou remova a referência para Resposta, dependendo do seu modelo de dados

    private String executar;

    // Adicionar o novo campo link
    @Column
    private String link;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "acao", cascade = CascadeType.ALL)
    private List<Rotina> rotinas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecutar() {
        return executar;
    }


    public void setExecutar(String executar) {
        this.executar = executar;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Rotina> getRotinas() {
        return rotinas;
    }

    public void setRotinas(List<Rotina> rotinas) {
        this.rotinas = rotinas;
    }
}

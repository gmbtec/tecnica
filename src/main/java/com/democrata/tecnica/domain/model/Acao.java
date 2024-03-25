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

    @OneToMany(mappedBy = "acao")
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

    public List<Rotina> getRotinas() {
        return rotinas;
    }

    public void setRotinas(List<Rotina> rotinas) {
        this.rotinas = rotinas;
    }
}

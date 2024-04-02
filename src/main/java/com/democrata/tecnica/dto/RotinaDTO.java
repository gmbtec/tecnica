package com.democrata.tecnica.dto;

import java.util.*;

public class RotinaDTO {
    private Long id;
    private String executarRotina;
    private List<String> modulos;

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

    public List<String> getModulos() {
        return modulos;
    }

    public void setModulos(List<String> modulos) {
        this.modulos = modulos;
    }

}

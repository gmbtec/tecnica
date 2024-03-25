package com.democrata.tecnica.domain.model;

import jakarta.persistence.*;
import java.util.List;

@Entity(name = "tb_resposta")
public class Resposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_questao")
    private Questao questao;

    private String descricao;

    @ManyToOne // Mudança aqui para ManyToOne
    @JoinColumn(name = "fk_acao") // Adicione a chave estrangeira para a tabela de Acao
    private Acao acao; // Mudança aqui para referenciar a entidade Acao

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }
}

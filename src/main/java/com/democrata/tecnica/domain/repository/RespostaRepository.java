package com.democrata.tecnica.domain.repository;

import com.democrata.tecnica.domain.model.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    Resposta findByDescricao(String descricao);

    List<Resposta> findByDescricaoContainingIgnoreCase(String pergunta);
}
package com.democrata.tecnica.domain.repository;

import com.democrata.tecnica.domain.model.Questao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoRepository extends JpaRepository<Questao, Long> {
}


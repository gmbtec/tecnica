package com.democrata.tecnica.domain.repository;

import com.democrata.tecnica.domain.model.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcaoRepository extends JpaRepository<Acao, Long> {
}

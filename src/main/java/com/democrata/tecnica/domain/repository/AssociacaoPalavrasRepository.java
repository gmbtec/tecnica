package com.democrata.tecnica.domain.repository;

import com.democrata.tecnica.domain.model.AssociacaoPalavras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociacaoPalavrasRepository extends JpaRepository<AssociacaoPalavras, Long> {
}
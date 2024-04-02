package com.democrata.tecnica.domain.repository;

import com.democrata.tecnica.domain.model.Rotina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RotinaRepository extends JpaRepository<Rotina, Long> {
    List<Rotina> findByExecutarRotina(String executarRotina);
}


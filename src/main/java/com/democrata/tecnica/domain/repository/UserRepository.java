package com.democrata.tecnica.domain.repository;

import com.democrata.tecnica.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Extendendo de JPA REPOSITORIO, o primeiro parametro é o USER e do tipo LONG
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByAccountNumber(String accountNumber);
}

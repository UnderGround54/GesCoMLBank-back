package org.gescomlbank.repositories;

import jakarta.transaction.Transactional;
import org.gescomlbank.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByNumAccount(@Param("numAccount") String numAccount);
}

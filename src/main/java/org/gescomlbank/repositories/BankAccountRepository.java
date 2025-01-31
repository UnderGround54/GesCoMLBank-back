package org.gescomlbank.repositories;

import jakarta.transaction.Transactional;
import org.gescomlbank.entities.BankAccount;
import org.gescomlbank.entities.CurrentAccount;
import org.gescomlbank.entities.SavingAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByNumAccount(@Param("numAccount") String numAccount);

    @Query("SELECT s FROM SavingAccount s")
    Page<SavingAccount> findSavingAccounts(Pageable pageable);

    @Query("SELECT c FROM CurrentAccount c")
    Page<CurrentAccount> findCurrentAccounts(Pageable pageable);
}

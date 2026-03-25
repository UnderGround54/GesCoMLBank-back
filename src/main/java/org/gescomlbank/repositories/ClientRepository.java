package org.gescomlbank.repositories;

import jakarta.transaction.Transactional;
import org.gescomlbank.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("""
        SELECT c FROM Client c
        WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(c.lastName)  LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(c.telephone) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(c.address)   LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(c.mail)     LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    Page<Client> searchClients(@Param("search") String search, Pageable pageable);
}

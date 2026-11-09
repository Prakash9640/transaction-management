package com.microservices.transactionmanagement.repo;

import com.microservices.transactionmanagement.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select a from Account a where a.id in :accountNumbers ORDER BY a.id")
    List<Account> findByAccountNumber(@Param("accountNumbers") List<Long> accountNumbers);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id = :id")
     Optional<Account> findAccountForUpdate(
            @Param("id") Long id);
}

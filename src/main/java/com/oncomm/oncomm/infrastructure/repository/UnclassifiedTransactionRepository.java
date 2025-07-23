package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnclassifiedTransactionRepository extends JpaRepository<UnclassifiedTransaction, Long> {
    @Query("""
    SELECT ut FROM UnclassifiedTransaction ut
    JOIN FETCH ut.transaction t
    WHERE ut.company.companyId = :companyId OR ut.company IS NULL
""")
    List<UnclassifiedTransaction> findWithTransactionByCompany_CompanyId(@Param("companyId") String companyId);

    @Query("""
    SELECT ut FROM UnclassifiedTransaction ut
    JOIN FETCH ut.transaction t
""")
    List<UnclassifiedTransaction> findAllWithTransaction();


    @Query("""
    SELECT CASE WHEN COUNT(ut) > 0 THEN true ELSE false END
    FROM UnclassifiedTransaction ut
    WHERE ut.transaction.occurredAt = :occurredAt
      AND ut.transaction.description = :description
      AND ut.transaction.deposit = :deposit
      AND ut.transaction.withdraw = :withdraw
""")
    boolean existsByTransactionFields(
            @Param("occurredAt") java.time.LocalDateTime occurredAt,
            @Param("description") String description,
            @Param("deposit") long deposit,
            @Param("withdraw") long withdraw
    );
}
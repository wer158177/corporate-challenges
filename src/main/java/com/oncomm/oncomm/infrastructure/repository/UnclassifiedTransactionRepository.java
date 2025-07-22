package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnclassifiedTransactionRepository extends JpaRepository<UnclassifiedTransaction, Long> {
}
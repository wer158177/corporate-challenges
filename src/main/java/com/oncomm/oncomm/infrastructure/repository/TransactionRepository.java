package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
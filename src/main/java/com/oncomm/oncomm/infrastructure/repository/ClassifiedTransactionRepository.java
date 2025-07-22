package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassifiedTransactionRepository extends JpaRepository<ClassifiedTransaction, Long> {
    List<ClassifiedTransaction> findAllByCompany_CompanyIdOrderByClassifiedAtDesc(String companyId);
}
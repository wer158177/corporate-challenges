package com.oncomm.oncomm.infrastructure.repository;

import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnclassifiedTransactionRepository extends JpaRepository<UnclassifiedTransaction, Long> {
    // ✅ 회사 ID로 미분류 거래 조회
    List<UnclassifiedTransaction> findByCompany_CompanyId(String companyId);
}
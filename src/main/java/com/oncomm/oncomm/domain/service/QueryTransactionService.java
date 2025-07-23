package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.dto.response.UnclassifiedTransactionResponse;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class QueryTransactionService {

    private final ClassifiedTransactionRepository classifiedTransactionRepository;
    private final UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    public QueryTransactionService(ClassifiedTransactionRepository classifiedTransactionRepository, UnclassifiedTransactionRepository unclassifiedTransactionRepository) {
        this.classifiedTransactionRepository = classifiedTransactionRepository;
        this.unclassifiedTransactionRepository = unclassifiedTransactionRepository;
    }

    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        return classifiedTransactionRepository.findAllWithJoinsByCompanyId(companyId).stream()
                .map(ClassifiedTransactionResponse::from)
                .toList();
    }

    public List<UnclassifiedTransactionResponse> getUnclassifiedTransactions(String companyId) {
        List<UnclassifiedTransaction> transactions;

        if ("__all__".equals(companyId) || companyId == null) {
            // 모든 미분류 거래 (회사 포함 + 회사 없음)
            transactions = unclassifiedTransactionRepository.findAllWithTransaction();
        } else {
            // 특정 회사 + 회사 정보 없는 거래
            transactions = unclassifiedTransactionRepository.findWithTransactionByCompany_CompanyId(companyId);
        }

        return transactions.stream()
                .map(UnclassifiedTransactionResponse::from)
                .toList();
    }


}

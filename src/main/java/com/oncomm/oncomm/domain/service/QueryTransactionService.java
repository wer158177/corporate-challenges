package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryTransactionService {

    private final ClassifiedTransactionRepository classifiedTransactionRepository;

    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        List<ClassifiedTransaction> txs = classifiedTransactionRepository.findAllWithJoinsByCompanyId(companyId);
        return txs.stream()
                .map(ClassifiedTransactionResponse::fromEntity)
                .toList();
    }

}
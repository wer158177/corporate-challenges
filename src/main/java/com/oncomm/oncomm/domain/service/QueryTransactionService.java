package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.dto.response.UnclassifiedTransactionResponse;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryTransactionService {

    private final ClassifiedTransactionRepository classifiedTransactionRepository;
    private final UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        List<ClassifiedTransaction> txs = classifiedTransactionRepository.findAllWithJoinsByCompanyId(companyId);
        return txs.stream()
                .map(ClassifiedTransactionResponse::fromEntity)
                .toList();
    }



    public List<UnclassifiedTransactionResponse> getUnclassifiedTransactions(String companyId) {
        return unclassifiedTransactionRepository.findByCompany_CompanyId(companyId)
                .stream()
                .map(utx -> new UnclassifiedTransactionResponse(
                        utx.getTransaction().getTxDatetime().toString(),
                        utx.getTransaction().getDescription(),
                        utx.getTransaction().getDeposit(),
                        utx.getTransaction().getWithdraw()
                ))
                .toList();
    }
}
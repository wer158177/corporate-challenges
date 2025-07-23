package com.oncomm.oncomm.application;


import com.oncomm.oncomm.domain.service.QueryTransactionService;
import com.oncomm.oncomm.domain.service.TransactionSummaryService;
import com.oncomm.oncomm.dto.response.AccountingSummary;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.dto.response.UnclassifiedTransactionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionQueryApplicationService {
    private final QueryTransactionService queryService;
    private final TransactionSummaryService summaryService;

    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        return queryService.getClassifiedTransactions(companyId);
    }

    public List<AccountingSummary> getTransactionSummary(String companyId) {
        return summaryService.getSummaryByCompany(companyId);
    }

    public AccountingSummary getTotalTransactionSummary(String companyId) {
        return summaryService.getTotalSummaryByCompany(companyId);
    }

    public List<UnclassifiedTransactionResponse> getUnclassifiedTransactions(String companyId) {
        return queryService.getUnclassifiedTransactions(companyId);
    }
}


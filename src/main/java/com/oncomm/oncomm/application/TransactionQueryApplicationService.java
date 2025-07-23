package com.oncomm.oncomm.application;


import com.oncomm.oncomm.domain.service.QueryTransactionService;
import com.oncomm.oncomm.domain.service.TransactionSummaryService;
import com.oncomm.oncomm.dto.response.AccountingSummary;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.dto.response.UnclassifiedTransactionResponse;
import com.oncomm.oncomm.exception.CompanyNotFoundException;
import com.oncomm.oncomm.infrastructure.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TransactionQueryApplicationService {
    private final QueryTransactionService queryService;
    private final TransactionSummaryService summaryService;
    private final CompanyRepository companyRepository;

    public TransactionQueryApplicationService(QueryTransactionService queryService, TransactionSummaryService summaryService, CompanyRepository companyRepository) {
        this.queryService = queryService;
        this.summaryService = summaryService;
        this.companyRepository = companyRepository;
    }


    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        validateCompanyExists(companyId);
        return queryService.getClassifiedTransactions(companyId);
    }

    public List<AccountingSummary> getTransactionSummary(String companyId) {
        validateCompanyExists(companyId);
        return summaryService.getSummaryByCompany(companyId);
    }

    public AccountingSummary getTotalTransactionSummary(String companyId) {
        validateCompanyExists(companyId);
        return summaryService.getTotalSummaryByCompany(companyId);
    }

    public List<UnclassifiedTransactionResponse> getUnclassifiedTransactions(String companyId) {
        validateCompanyExists(companyId);
        return queryService.getUnclassifiedTransactions(companyId);
    }



    private void validateCompanyExists(String companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));
    }
}


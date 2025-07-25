package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.dto.response.AccountingSummary;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@Transactional
public class TransactionSummaryService {
    private final ClassifiedTransactionRepository classifiedTransactionRepository;

    public TransactionSummaryService(ClassifiedTransactionRepository classifiedTransactionRepository) {
        this.classifiedTransactionRepository = classifiedTransactionRepository;
    }

    public List<AccountingSummary> getSummaryByCompany(String companyId) {
        return classifiedTransactionRepository.getAccountingSummary(companyId);
    }

    public AccountingSummary getTotalSummaryByCompany(String companyId) {
        List<AccountingSummary> summaries = getSummaryByCompany(companyId);

        return new AccountingSummary(
                "TOTAL",
                "전체",
                summaries.stream().mapToLong(AccountingSummary::getTotalIncome).sum(),
                summaries.stream().mapToLong(AccountingSummary::getTotalExpenditure).sum()
        );
    }
}

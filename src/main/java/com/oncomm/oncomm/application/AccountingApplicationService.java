
package com.oncomm.oncomm.application;

import com.oncomm.oncomm.domain.model.*;
import com.oncomm.oncomm.domain.service.*;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.util.ClassificationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingApplicationService {

    private final RuleIngestService ruleIngestService;
    private final CsvParserService csvParserService;
    private final TransactionClassifierService transactionClassifierService;
    private final ResultPersistenceService resultPersistenceService;
    private final QueryTransactionService queryTransactionService;

    public void process(MultipartFile csvFile, MultipartFile jsonFile) {
        ruleIngestService.saveRules(jsonFile);
        List<Transaction> transactions = csvParserService.parseTransactions(csvFile);
        ClassificationResult result = transactionClassifierService.classify(transactions);
        resultPersistenceService.persist(result);
    }

    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        return queryTransactionService.getClassifiedTransactions(companyId);
    }


}

package com.oncomm.oncomm.application;

import com.oncomm.oncomm.domain.model.*;
import com.oncomm.oncomm.domain.service.*;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.util.ClassificationResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountingApplicationService {

    private final RuleIngestService ruleIngestService;
    private final CsvParserService csvParserService;
    private final TransactionClassifierService classifierService;
    private final TransactionPersistenceService persistenceService;
    private final QueryTransactionService queryTransactionService;


    @Transactional
    public void processTransactions(MultipartFile csvFile, MultipartFile jsonFile) {
        ruleIngestService.saveRules(jsonFile);
        List<Transaction> transactions = csvParserService.parseTransactions(csvFile);
        ClassificationResult result = classifierService.classify(transactions);
        persistenceService.persistClassificationResult(result);
    }


    public List<ClassifiedTransactionResponse> getClassifiedTransactions(String companyId) {
        return queryTransactionService.getClassifiedTransactions(companyId);
    }




}
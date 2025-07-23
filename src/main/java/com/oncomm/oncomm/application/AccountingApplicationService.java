
package com.oncomm.oncomm.application;

import com.oncomm.oncomm.domain.model.Transaction;
import com.oncomm.oncomm.domain.service.CsvParserService;
import com.oncomm.oncomm.domain.service.RuleIngestService;
import com.oncomm.oncomm.domain.service.TransactionClassifierService;
import com.oncomm.oncomm.domain.service.TransactionPersistenceService;
import com.oncomm.oncomm.util.ClassificationResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service

public class AccountingApplicationService {

    private final RuleIngestService ruleIngestService;
    private final CsvParserService csvParserService;
    private final TransactionClassifierService classifierService;
    private final TransactionPersistenceService persistenceService;

    public AccountingApplicationService(RuleIngestService ruleIngestService, CsvParserService csvParserService, TransactionClassifierService classifierService, TransactionPersistenceService persistenceService) {
        this.ruleIngestService = ruleIngestService;
        this.csvParserService = csvParserService;
        this.classifierService = classifierService;
        this.persistenceService = persistenceService;
    }


    @Transactional
    public void processTransactions(MultipartFile csvFile, MultipartFile jsonFile) {
        ruleIngestService.saveRules(jsonFile);
        List<Transaction> transactions = csvParserService.parseTransactions(csvFile);
        ClassificationResult result = classifierService.classify(transactions);
        persistenceService.persistClassificationResult(result);
    }






}
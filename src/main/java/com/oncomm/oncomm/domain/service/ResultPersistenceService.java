package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import com.oncomm.oncomm.util.ClassificationResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service

public class ResultPersistenceService {

    private final ClassifiedTransactionRepository classifiedTransactionRepository;
    private final UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    public ResultPersistenceService(ClassifiedTransactionRepository classifiedTransactionRepository, UnclassifiedTransactionRepository unclassifiedTransactionRepository) {
        this.classifiedTransactionRepository = classifiedTransactionRepository;
        this.unclassifiedTransactionRepository = unclassifiedTransactionRepository;
    }

    @Transactional
    public void persist(ClassificationResult result) {
        if (!result.classified().isEmpty()) {
            classifiedTransactionRepository.saveAll(result.classified());
        }
        if (!result.unclassified().isEmpty()) {
            unclassifiedTransactionRepository.saveAll(result.unclassified());
        }
    }
}
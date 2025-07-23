package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import com.oncomm.oncomm.util.ClassificationResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionPersistenceService {
    private final ClassifiedTransactionRepository classifiedTransactionRepository;
    private final UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    public void persistClassificationResult(ClassificationResult result) {
        if (!result.classified().isEmpty()) {
            classifiedTransactionRepository.saveAll(result.classified());
        }
        if (!result.unclassified().isEmpty()) {
            unclassifiedTransactionRepository.saveAll(result.unclassified());
        }
    }
}

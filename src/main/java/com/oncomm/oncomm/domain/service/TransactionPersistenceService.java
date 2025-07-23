package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import com.oncomm.oncomm.dto.response.ClassificationPersistenceResult;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import com.oncomm.oncomm.util.ClassificationResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional

public class TransactionPersistenceService {

    private final ClassifiedTransactionRepository classifiedTransactionRepository;
    private final UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    public TransactionPersistenceService(ClassifiedTransactionRepository classifiedTransactionRepository, UnclassifiedTransactionRepository unclassifiedTransactionRepository) {
        this.classifiedTransactionRepository = classifiedTransactionRepository;
        this.unclassifiedTransactionRepository = unclassifiedTransactionRepository;
    }

    public ClassificationPersistenceResult persistClassificationResult(ClassificationResult result) {
        List<ClassifiedTransaction> savedClassified = new ArrayList<>();
        List<ClassifiedTransaction> skippedClassified = new ArrayList<>();

        for (ClassifiedTransaction tx : result.classified()) {
            boolean exists = classifiedTransactionRepository.existsByTransactionFields(
                    tx.getOccurredAt(), tx.getDescription(), tx.getDeposit(), tx.getWithdraw()
            );
            if (exists) {
                skippedClassified.add(tx);
            } else {
                savedClassified.add(tx);
            }
        }

        List<UnclassifiedTransaction> savedUnclassified = new ArrayList<>();
        List<UnclassifiedTransaction> skippedUnclassified = new ArrayList<>();

        for (UnclassifiedTransaction tx : result.unclassified()) {
            boolean exists = unclassifiedTransactionRepository.existsByTransactionFields(
                    tx.getOccurredAt(), tx.getDescription(), tx.getDeposit(), tx.getWithdraw()
            );
            if (exists) {
                skippedUnclassified.add(tx);
            } else {
                savedUnclassified.add(tx);
            }
        }

        if (!savedClassified.isEmpty()) {
            classifiedTransactionRepository.saveAll(savedClassified);
        }

        if (!savedUnclassified.isEmpty()) {
            unclassifiedTransactionRepository.saveAll(savedUnclassified);
        }

        return new ClassificationPersistenceResult(
                savedClassified, savedUnclassified, skippedClassified, skippedUnclassified
        );
    }


}

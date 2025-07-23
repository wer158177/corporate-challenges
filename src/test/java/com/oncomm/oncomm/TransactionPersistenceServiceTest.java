package com.oncomm.oncomm;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import com.oncomm.oncomm.domain.service.TransactionPersistenceService;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import com.oncomm.oncomm.util.ClassificationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

class TransactionPersistenceServiceTest {

    @Mock
    private ClassifiedTransactionRepository classifiedTransactionRepository;

    @Mock
    private UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    @InjectMocks
    private TransactionPersistenceService transactionPersistenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void persistClassificationResult_분류_미분류_모두저장() {
        var classified = List.of(mock(ClassifiedTransaction.class));
        var unclassified = List.of(mock(UnclassifiedTransaction.class));
        ClassificationResult result = new ClassificationResult(classified, unclassified);

        transactionPersistenceService.persistClassificationResult(result);

        verify(classifiedTransactionRepository).saveAll(classified);
        verify(unclassifiedTransactionRepository).saveAll(unclassified);
    }

    @Test
    void persistClassificationResult_분류만저장() {
        var classified = List.of(mock(ClassifiedTransaction.class));
        ClassificationResult result = new ClassificationResult(classified, List.of());

        transactionPersistenceService.persistClassificationResult(result);

        verify(classifiedTransactionRepository).saveAll(classified);
        verify(unclassifiedTransactionRepository, never()).saveAll(any());
    }

    @Test
    void persistClassificationResult_미분류만저장() {
        var unclassified = List.of(mock(UnclassifiedTransaction.class));
        ClassificationResult result = new ClassificationResult(List.of(), unclassified);

        transactionPersistenceService.persistClassificationResult(result);

        verify(classifiedTransactionRepository, never()).saveAll(any());
        verify(unclassifiedTransactionRepository).saveAll(unclassified);
    }

    @Test
    void persistClassificationResult_모두비어있으면저장안함() {
        ClassificationResult result = new ClassificationResult(List.of(), List.of());

        transactionPersistenceService.persistClassificationResult(result);

        verify(classifiedTransactionRepository, never()).saveAll(any());
        verify(unclassifiedTransactionRepository, never()).saveAll(any());
    }
}

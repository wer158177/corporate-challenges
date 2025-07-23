package com.oncomm.oncomm;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import com.oncomm.oncomm.domain.service.ResultPersistenceService;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import com.oncomm.oncomm.util.ClassificationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class ResultPersistenceServiceTest {

    @Mock
    private ClassifiedTransactionRepository classifiedTransactionRepository;

    @Mock
    private UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    @InjectMocks
    private ResultPersistenceService resultPersistenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void persist_분류와미분류모두존재() {
        // given
        List<ClassifiedTransaction> classifiedList = List.of(mock(ClassifiedTransaction.class));
        List<UnclassifiedTransaction> unclassifiedList = List.of(mock(UnclassifiedTransaction.class));
        ClassificationResult result = new ClassificationResult(classifiedList, unclassifiedList);

        // when
        resultPersistenceService.persist(result);

        // then
        verify(classifiedTransactionRepository, times(1)).saveAll(classifiedList);
        verify(unclassifiedTransactionRepository, times(1)).saveAll(unclassifiedList);
    }

    @Test
    void persist_분류만존재() {
        List<ClassifiedTransaction> classifiedList = List.of(mock(ClassifiedTransaction.class));
        ClassificationResult result = new ClassificationResult(classifiedList, Collections.emptyList());

        resultPersistenceService.persist(result);

        verify(classifiedTransactionRepository, times(1)).saveAll(classifiedList);
        verify(unclassifiedTransactionRepository, never()).saveAll(any());
    }

    @Test
    void persist_미분류만존재() {
        List<UnclassifiedTransaction> unclassifiedList = List.of(mock(UnclassifiedTransaction.class));
        ClassificationResult result = new ClassificationResult(Collections.emptyList(), unclassifiedList);

        resultPersistenceService.persist(result);

        verify(classifiedTransactionRepository, never()).saveAll(any());
        verify(unclassifiedTransactionRepository, times(1)).saveAll(unclassifiedList);
    }

    @Test
    void persist_모두비어있음() {
        ClassificationResult result = new ClassificationResult(Collections.emptyList(), Collections.emptyList());

        resultPersistenceService.persist(result);

        verify(classifiedTransactionRepository, never()).saveAll(any());
        verify(unclassifiedTransactionRepository, never()).saveAll(any());
    }
}

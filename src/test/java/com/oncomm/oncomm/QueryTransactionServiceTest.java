package com.oncomm.oncomm;

import com.oncomm.oncomm.domain.model.*;
import com.oncomm.oncomm.domain.service.QueryTransactionService;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;
import com.oncomm.oncomm.dto.response.UnclassifiedTransactionResponse;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import com.oncomm.oncomm.infrastructure.repository.UnclassifiedTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class QueryTransactionServiceTest {

    @Mock
    private ClassifiedTransactionRepository classifiedTransactionRepository;

    @Mock
    private UnclassifiedTransactionRepository unclassifiedTransactionRepository;

    @InjectMocks
    private QueryTransactionService queryTransactionService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    private Transaction sampleTransaction() {
        return Transaction.builder()
                .occurredAt(LocalDateTime.of(2025, 7, 23, 14, 30))
                .description("샘플거래")
                .deposit(1000L)
                .withdraw(0L)
                .balance(1010000L)
                .branchInfo("강남지점")
                .build();
    }

    @Test
    void getClassifiedTransactions_정상조회() {
        // given
        ClassifiedTransaction tx = ClassifiedTransaction.builder()
                .transaction(sampleTransaction())
                .category(Category.builder()
                        .categoryId("cat_101")
                        .categoryName("매출")
                        .build())
                .build();

        when(classifiedTransactionRepository.findAllWithJoinsByCompanyId("com_1"))
                .thenReturn(List.of(tx));

        // when
        List<ClassifiedTransactionResponse> result = queryTransactionService.getClassifiedTransactions("com_1");

        // then
        assertThat(result).hasSize(1);
        verify(classifiedTransactionRepository).findAllWithJoinsByCompanyId("com_1");
    }

    @Test
    void getUnclassifiedTransactions_전체조회_all() {
        // given
        UnclassifiedTransaction utx = UnclassifiedTransaction.builder()
                .transaction(sampleTransaction())
                .reason("테스트 이유")
                .reviewed(false)
                .build();

        when(unclassifiedTransactionRepository.findAllWithTransaction())
                .thenReturn(List.of(utx));

        // when
        List<UnclassifiedTransactionResponse> result = queryTransactionService.getUnclassifiedTransactions("__all__");

        // then
        assertThat(result).hasSize(1);
        verify(unclassifiedTransactionRepository).findAllWithTransaction();
    }

    @Test
    void getUnclassifiedTransactions_전체조회_null도동일하게() {
        // given
        UnclassifiedTransaction utx = UnclassifiedTransaction.builder()
                .transaction(sampleTransaction())
                .reason("테스트 이유")
                .reviewed(false)
                .build();

        when(unclassifiedTransactionRepository.findAllWithTransaction())
                .thenReturn(List.of(utx));

        // when
        List<UnclassifiedTransactionResponse> result = queryTransactionService.getUnclassifiedTransactions(null);

        // then
        assertThat(result).hasSize(1);
        verify(unclassifiedTransactionRepository).findAllWithTransaction();
    }

    @Test
    void getUnclassifiedTransactions_회사조건조회() {
        // given
        UnclassifiedTransaction utx = UnclassifiedTransaction.builder()
                .transaction(sampleTransaction())
                .reason("쿠팡 관련 거래")
                .reviewed(false)
                .build();

        when(unclassifiedTransactionRepository.findWithTransactionByCompany_CompanyId("com_2"))
                .thenReturn(List.of(utx));

        // when
        List<UnclassifiedTransactionResponse> result = queryTransactionService.getUnclassifiedTransactions("com_2");

        // then
        assertThat(result).hasSize(1);
        verify(unclassifiedTransactionRepository).findWithTransactionByCompany_CompanyId("com_2");
    }
}

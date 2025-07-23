package com.oncomm.oncomm;

import com.oncomm.oncomm.domain.service.TransactionSummaryService;
import com.oncomm.oncomm.dto.response.AccountingSummary;
import com.oncomm.oncomm.infrastructure.repository.ClassifiedTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionSummaryServiceTest {

    @Mock
    private ClassifiedTransactionRepository classifiedTransactionRepository;

    @InjectMocks
    private TransactionSummaryService transactionSummaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSummaryByCompany_호출확인() {
        String companyId = "com_1";
        var mockList = List.of(
                new AccountingSummary("cat_1", "매출", 100_000L, 0L),
                new AccountingSummary("cat_2", "식비", 0L, 50_000L)
        );

        when(classifiedTransactionRepository.getAccountingSummary(companyId)).thenReturn(mockList);

        var result = transactionSummaryService.getSummaryByCompany(companyId);

        assertThat(result).hasSize(2);
        verify(classifiedTransactionRepository).getAccountingSummary(companyId);
    }

    @Test
    void getTotalSummaryByCompany_정상집계() {
        // given
        String companyId = "com_1";
        var summaries = List.of(
                new AccountingSummary("cat_1", "매출", 120_000L, 0L),
                new AccountingSummary("cat_2", "통신비", 0L, 30_000L)
        );

        when(classifiedTransactionRepository.getAccountingSummary(companyId)).thenReturn(summaries);

        // when
        AccountingSummary total = transactionSummaryService.getTotalSummaryByCompany(companyId);

        // then
        assertThat(total.getCategoryId()).isEqualTo("TOTAL");
        assertThat(total.getCategoryName()).isEqualTo("전체");
        assertThat(total.getTotalIncome()).isEqualTo(120_000L);
        assertThat(total.getTotalExpenditure()).isEqualTo(30_000L);
    }
}

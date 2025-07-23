package com.oncomm.oncomm;

import com.oncomm.oncomm.domain.model.*;
import com.oncomm.oncomm.domain.service.TransactionClassifierService;
import com.oncomm.oncomm.infrastructure.repository.MerchantKeywordRepository;
import com.oncomm.oncomm.util.ClassificationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionClassifierServiceTest {

    @Mock
    private MerchantKeywordRepository merchantKeywordRepository;

    @InjectMocks
    private TransactionClassifierService transactionClassifierService;

    private Company sampleCompany;
    private Category sampleCategory;
    private ClassificationKeyword keyword1;
    private ClassificationKeyword keyword2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCompany = Company.builder().companyId("com_1").companyName("A 커머스").build();
        sampleCategory = Category.builder().categoryId("cat_101").categoryName("매출").company(sampleCompany).build();

        keyword1 = ClassificationKeyword.builder()
                .keyword("쿠팡")
                .company(sampleCompany)
                .category(sampleCategory)
                .build();

        keyword2 = ClassificationKeyword.builder()
                .keyword("네이버")
                .company(sampleCompany)
                .category(sampleCategory)
                .build();
    }

    private Transaction buildTx(String description, Company company) {
        return Transaction.builder()
                .id(1L)
                .description(description)
                .company(company)
                .occurredAt(LocalDateTime.now())
                .build();
    }

    @Test
    void classify_회사정보없음() {
        // given
        Transaction tx = buildTx("쿠팡 주문", null);
        when(merchantKeywordRepository.findAllWithJoins()).thenReturn(List.of(keyword1));

        // when
        ClassificationResult result = transactionClassifierService.classify(List.of(tx));

        // then
        assertThat(result.classified()).isEmpty();
        assertThat(result.unclassified()).hasSize(1);
        assertThat(result.unclassified().get(0).getReason()).contains("회사 정보 없음");
    }

    @Test
    void classify_설명없음() {
        Transaction tx = buildTx(" ", sampleCompany);
        when(merchantKeywordRepository.findAllWithJoins()).thenReturn(List.of(keyword1));

        ClassificationResult result = transactionClassifierService.classify(List.of(tx));

        assertThat(result.classified()).isEmpty();
        assertThat(result.unclassified()).hasSize(1);
        assertThat(result.unclassified().get(0).getReason()).contains("설명 누락");
    }

    @Test
    void classify_키워드매칭성공() {
        Transaction tx = buildTx("쿠팡 온라인몰 결제", sampleCompany);
        when(merchantKeywordRepository.findAllWithJoins()).thenReturn(List.of(keyword1));

        ClassificationResult result = transactionClassifierService.classify(List.of(tx));

        assertThat(result.classified()).hasSize(1);
        assertThat(result.unclassified()).isEmpty();
    }

    @Test
    void classify_키워드매칭실패() {
        Transaction tx = buildTx("배달의민족 주문", sampleCompany);
        when(merchantKeywordRepository.findAllWithJoins()).thenReturn(List.of(keyword1));

        ClassificationResult result = transactionClassifierService.classify(List.of(tx));

        assertThat(result.classified()).isEmpty();
        assertThat(result.unclassified()).hasSize(1);
        assertThat(result.unclassified().get(0).getReason()).contains("키워드 미일치");
    }

    @Test
    void classify_여러건혼합처리() {
        Transaction tx1 = buildTx("쿠팡 결제", sampleCompany);               // 매칭
        Transaction tx2 = buildTx("배달의민족", sampleCompany);            // 미일치
        Transaction tx3 = buildTx(" ", sampleCompany);                    // 설명 누락
        Transaction tx4 = buildTx("네이버 쇼핑", sampleCompany);          // 매칭
        Transaction tx5 = buildTx("카카오페이", null);                    // 회사 없음

        when(merchantKeywordRepository.findAllWithJoins()).thenReturn(List.of(keyword1, keyword2));

        ClassificationResult result = transactionClassifierService.classify(List.of(tx1, tx2, tx3, tx4, tx5));

        assertThat(result.classified()).hasSize(2); // tx1, tx4
        assertThat(result.unclassified()).hasSize(3); // tx2, tx3, tx5
    }
}

package com.oncomm.oncomm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncomm.oncomm.domain.model.Category;
import com.oncomm.oncomm.domain.model.ClassificationKeyword;
import com.oncomm.oncomm.domain.model.Company;
import com.oncomm.oncomm.domain.service.RuleIngestService;
import com.oncomm.oncomm.infrastructure.repository.CategoryRepository;
import com.oncomm.oncomm.infrastructure.repository.CompanyRepository;
import com.oncomm.oncomm.infrastructure.repository.MerchantKeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class RuleIngestServiceTest {

    @Mock private CompanyRepository companyRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private MerchantKeywordRepository merchantKeywordRepository;

    @InjectMocks private RuleIngestService ruleIngestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ruleIngestService = new RuleIngestService(
                new ObjectMapper(),
                companyRepository,
                categoryRepository,
                merchantKeywordRepository
        );
    }

    private MockMultipartFile validJsonFile() {
        String json = """
        {
          "companies": [
            {
              "company_id": "com_1",
              "company_name": "A 커머스",
              "categories": [
                {
                  "category_id": "cat_101",
                  "category_name": "매출",
                  "keywords": ["네이버페이", "쿠팡"]
                }
              ]
            }
          ]
        }
        """;
        return new MockMultipartFile("file", "rules.json", "application/json", json.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void saveRules_정상처리() {
        // given
        when(companyRepository.findById("com_1")).thenReturn(Optional.empty());
        when(categoryRepository.findById("cat_101")).thenReturn(Optional.empty());
        when(merchantKeywordRepository.findAllByCompanyAndCategory(any(), any())).thenReturn(Collections.emptyList());

        when(companyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(merchantKeywordRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        ruleIngestService.saveRules(validJsonFile());

        // then
        verify(companyRepository, times(1)).save(any(Company.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(merchantKeywordRepository, times(2)).save(any(ClassificationKeyword.class)); // 네이버페이, 쿠팡
    }

    @Test
    void saveRules_이미존재하는회사_저장안함() {
        // given
        Company existingCompany = Company.builder().companyId("com_1").companyName("A 커머스").build();
        Category existingCategory = Category.builder().categoryId("cat_101").categoryName("매출").company(existingCompany).build();

        when(companyRepository.findById("com_1")).thenReturn(Optional.of(existingCompany));
        when(categoryRepository.findById("cat_101")).thenReturn(Optional.of(existingCategory));
        when(merchantKeywordRepository.findAllByCompanyAndCategory(existingCompany, existingCategory))
                .thenReturn(List.of(ClassificationKeyword.builder().keyword("네이버페이").build()));

        // when
        ruleIngestService.saveRules(validJsonFile());

        // then
        verify(companyRepository, never()).save(any());
        verify(categoryRepository, never()).save(any());
        verify(merchantKeywordRepository, times(1)).save(any()); // 쿠팡만 저장
    }

    @Test
    void saveRules_키워드없으면저장안함() {
        // given
        String json = """
        {
          "companies": [
            {
              "company_id": "com_1",
              "company_name": "A 커머스",
              "categories": [
                {
                  "category_id": "cat_102",
                  "category_name": "식비",
                  "keywords": []
                }
              ]
            }
          ]
        }
        """;
        MockMultipartFile jsonFile = new MockMultipartFile("file", "rules.json", "application/json", json.getBytes(StandardCharsets.UTF_8));

        when(companyRepository.findById("com_1")).thenReturn(Optional.empty());
        when(categoryRepository.findById("cat_102")).thenReturn(Optional.empty());
        when(companyRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(categoryRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        ruleIngestService.saveRules(jsonFile);

        // then
        verify(merchantKeywordRepository, never()).save(any());
    }

    @Test
    void saveRules_JSON파싱실패() {
        // given: 잘못된 JSON
        String brokenJson = "{ invalid json ";
        MockMultipartFile brokenFile = new MockMultipartFile("file", "broken.json", "application/json", brokenJson.getBytes(StandardCharsets.UTF_8));

        // when & then
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> ruleIngestService.saveRules(brokenFile))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("rules.json 파싱 중 오류");
    }
}

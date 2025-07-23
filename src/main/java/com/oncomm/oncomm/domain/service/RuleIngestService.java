package com.oncomm.oncomm.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncomm.oncomm.domain.model.Category;
import com.oncomm.oncomm.domain.model.Company;
import com.oncomm.oncomm.domain.model.MerchantKeyword;
import com.oncomm.oncomm.infrastructure.repository.*;
import com.oncomm.oncomm.support.CompanyRule;
import com.oncomm.oncomm.support.Rule;
import com.oncomm.oncomm.support.RuleFile;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuleIngestService {

    private final ObjectMapper objectMapper;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantKeywordRepository merchantKeywordRepository;

    @Transactional
    public void saveRules(MultipartFile jsonFile) {
        try {
            RuleFile ruleFile = objectMapper.readValue(jsonFile.getInputStream(), RuleFile.class);

            for (CompanyRule companyRule : ruleFile.getCompanies()) {
                String companyId = companyRule.getCompanyId();
                String companyName = companyRule.getCompanyName();

                Company company = companyRepository.findById(companyId).orElseGet(() ->
                        companyRepository.save(
                                Company.builder()
                                        .companyId(companyId)
                                        .companyName(companyName)
                                        .createdAt(LocalDateTime.now())
                                        .build()
                        )
                );

                for (Rule rule : companyRule.getCategories()) {
                    String categoryId = rule.getCategoryId();
                    String categoryName = rule.getCategoryName();

                    Category category = categoryRepository.findById(categoryId).orElseGet(() ->
                            categoryRepository.save(
                                    Category.builder()
                                            .categoryId(categoryId)
                                            .categoryName(categoryName)
                                            .company(company)
                                            .createdAt(LocalDateTime.now())
                                            .build()
                            )
                    );

                    if (rule.getKeywords() != null) {
                        for (String keyword : rule.getKeywords()) {
                            merchantKeywordRepository.findByCompanyAndCategoryAndKeyword(company, category, keyword)
                                    .orElseGet(() -> merchantKeywordRepository.save(
                                            MerchantKeyword.builder()
                                                    .company(company)
                                                    .category(category)
                                                    .keyword(keyword)
                                                    .createdAt(LocalDateTime.now())
                                                    .build()
                                    ));
                        }
                    }
                }
            }

            log.info("[RuleIngestService] rules.json 파싱 및 저장 완료 (신규만)");

        } catch (IOException e) {
            log.error("[RuleIngestService] rules.json 파싱 실패", e);
            throw new RuntimeException("rules.json 파싱 중 오류가 발생했습니다.", e);
        }
    }
}

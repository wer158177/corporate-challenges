package com.oncomm.oncomm.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncomm.oncomm.domain.model.Category;
import com.oncomm.oncomm.domain.model.ClassificationKeyword;
import com.oncomm.oncomm.domain.model.Company;
import com.oncomm.oncomm.infrastructure.repository.CategoryRepository;
import com.oncomm.oncomm.infrastructure.repository.CompanyRepository;
import com.oncomm.oncomm.infrastructure.repository.MerchantKeywordRepository;
import com.oncomm.oncomm.support.CompanyRule;
import com.oncomm.oncomm.support.Rule;
import com.oncomm.oncomm.support.RuleFile;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service

public class RuleIngestService {

    private final ObjectMapper objectMapper;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final MerchantKeywordRepository merchantKeywordRepository;

    public RuleIngestService(ObjectMapper objectMapper, CompanyRepository companyRepository, CategoryRepository categoryRepository, MerchantKeywordRepository merchantKeywordRepository) {
        this.objectMapper = objectMapper;
        this.companyRepository = companyRepository;
        this.categoryRepository = categoryRepository;
        this.merchantKeywordRepository = merchantKeywordRepository;
    }

    @Transactional
    public void saveRules(MultipartFile jsonFile) {
        try {
            RuleFile ruleFile = objectMapper.readValue(jsonFile.getInputStream(), RuleFile.class);

            Map<String, Company> companyCache = new HashMap<>();
            Map<String, Category> categoryCache = new HashMap<>();

            for (CompanyRule companyRule : ruleFile.getCompanies()) {
                String companyId = companyRule.getCompanyId();
                String companyName = companyRule.getCompanyName();

                Company company = companyCache.computeIfAbsent(companyId, id ->
                        companyRepository.findById(id).orElseGet(() ->
                                companyRepository.save(
                                        Company.builder()
                                                .companyId(companyId)
                                                .companyName(companyName)
                                                .createdAt(LocalDateTime.now())
                                                .build()
                                )
                        )
                );

                for (Rule rule : companyRule.getCategories()) {
                    String categoryId = rule.getCategoryId();
                    String categoryName = rule.getCategoryName();

                    String categoryKey = companyId + ":" + categoryId;
                    Category category = categoryCache.computeIfAbsent(categoryKey, key ->
                            categoryRepository.findById(categoryId).orElseGet(() ->
                                    categoryRepository.save(
                                            Category.builder()
                                                    .categoryId(categoryId)
                                                    .categoryName(categoryName)
                                                    .company(company)
                                                    .createdAt(LocalDateTime.now())
                                                    .build()
                                    )
                            )
                    );

                    if (rule.getKeywords() != null && !rule.getKeywords().isEmpty()) {
                        Set<String> existingKeywords = merchantKeywordRepository
                                .findAllByCompanyAndCategory(company, category)
                                .stream()
                                .map(ClassificationKeyword::getKeyword)
                                .collect(Collectors.toSet());

                        for (String keyword : rule.getKeywords()) {
                            if (!existingKeywords.contains(keyword)) {
                                merchantKeywordRepository.save(
                                        ClassificationKeyword.builder()
                                                .company(company)
                                                .category(category)
                                                .keyword(keyword)
                                                .createdAt(LocalDateTime.now())
                                                .build()
                                );
                            }
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

package com.oncomm.oncomm.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oncomm.oncomm.domain.model.*;
import com.oncomm.oncomm.exception.CategoryNotFoundException;
import com.oncomm.oncomm.exception.CompanyNotFoundException;
import com.oncomm.oncomm.exception.CsvParsingException;
import com.oncomm.oncomm.exception.JsonParsingException;
import com.oncomm.oncomm.infrastructure.repository.*;
import com.oncomm.oncomm.support.Rule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountingService {

    private final TransactionRepository transactionRepository;
    private final MerchantKeywordRepository keywordRepository;
    private final CompanyRepository companyRepository;
    private final CategoryRepository categoryRepository;
    private final ClassifiedTransactionRepository classifiedRepository;
    private final UnclassifiedTransactionRepository unclassifiedRepository;

    public void processCsvAndJson(MultipartFile csvFile, MultipartFile jsonFile) {
        List<Transaction> transactions = parseCsv(csvFile);
        Map<String, List<Rule>> rulesByCompany = parseJson(jsonFile);

        for (Transaction tx : transactions) {
            boolean matched = false;

            for (Map.Entry<String, List<Rule>> entry : rulesByCompany.entrySet()) {
                String companyId = entry.getKey();
                List<Rule> rules = entry.getValue();

                for (Rule rule : rules) {
                    for (String keyword : rule.getKeywords()) {
                        if (tx.getDescription().contains(keyword)) {

                            Company company = companyRepository.findById(companyId)
                                    .orElseThrow(() -> new CompanyNotFoundException(companyId));

                            Category category = categoryRepository.findById(rule.getCategoryId())
                                    .orElseThrow(() -> new CategoryNotFoundException(rule.getCategoryId()));

                            ClassifiedTransaction classified = ClassifiedTransaction.builder()
                                    .txId(tx.getTxId())
                                    .company(company)
                                    .companyName(company.getCompanyName())
                                    .category(category)
                                    .categoryName(category.getCategoryName())
                                    .matchedKeyword(keyword)
                                    .classifiedAt(LocalDateTime.now())
                                    .build();

                            classifiedRepository.save(classified);
                            matched = true;
                            break;
                        }
                    }
                    if (matched) break;
                }
                if (matched) break;
            }

            if (!matched) {
                UnclassifiedTransaction unclassified = UnclassifiedTransaction.builder()
                        .txId(tx.getTxId())
                        .reason("키워드 매칭 실패")
                        .reviewed(false)
                        .build();
                unclassifiedRepository.save(unclassified);
            }
        }
    }

    public List<ClassifiedTransaction> getClassifiedByCompany(String companyId) {
        return classifiedRepository.findAllByCompany_CompanyIdOrderByClassifiedAtDesc(companyId);
    }

    private List<Transaction> parseCsv(MultipartFile csvFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().skip(1).map(line -> {
                String[] cols = line.split(",");
                return Transaction.builder()
                        .txDatetime(LocalDateTime.parse(cols[0].trim().replace(" ", "T")))
                        .description(cols[1].trim())
                        .deposit(parseLong(cols[2]))
                        .withdraw(parseLong(cols[3]))
                        .balance(parseLong(cols[4]))
                        .branchInfo(cols.length > 5 ? cols[5].trim() : null)
                        .createdAt(LocalDateTime.now())
                        .build();
            }).collect(Collectors.toList());
        } catch (Exception e) {
            throw new CsvParsingException("CSV 파싱 실패", e);
        }
    }

    private Map<String, List<Rule>> parseJson(MultipartFile jsonFile) {
        try {
            String json = new String(jsonFile.getBytes(), StandardCharsets.UTF_8);
            return new ObjectMapper()
                    .readerFor(new TypeReference<Map<String, List<Rule>>>() {})
                    .readValue(json);
        } catch (Exception e) {
            throw new JsonParsingException("JSON 파싱 실패", e);
        }
    }

    private Long parseLong(String s) {
        try {
            return Long.parseLong(s.replaceAll("[^\\d]", ""));
        } catch (Exception e) {
            return 0L;
        }
    }
}

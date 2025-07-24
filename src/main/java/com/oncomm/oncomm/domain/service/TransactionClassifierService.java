package com.oncomm.oncomm.domain.service;

import com.oncomm.oncomm.domain.model.*;
import com.oncomm.oncomm.infrastructure.repository.MerchantKeywordRepository;
import com.oncomm.oncomm.util.ClassificationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionClassifierService {

    private final MerchantKeywordRepository merchantKeywordRepository;

    public ClassificationResult classify(List<Transaction> transactions) {
        List<ClassificationKeyword> allKeywords = merchantKeywordRepository.findAllWithJoins();
        List<ClassifiedTransaction> classifiedList = new ArrayList<>();
        List<UnclassifiedTransaction> unclassifiedList = new ArrayList<>();

        for (Transaction tx : transactions) {
            String description = tx.getDescription();

            // 설명 누락
            if (description == null || description.trim().isEmpty()) {
                unclassifiedList.add(UnclassifiedTransaction.from(tx, null, "설명 누락"));
                continue;
            }

            // 키워드 기반 매칭 (회사 없이)
            Optional<ClassificationKeyword> match = allKeywords.stream()
                    .filter(kw -> description.contains(kw.getKeyword()))
                    .findFirst();

            if (match.isPresent()) {
                classifiedList.add(ClassifiedTransaction.from(tx, match.get()));
            } else {
                unclassifiedList.add(UnclassifiedTransaction.from(tx, null, "키워드 미일치"));
            }
        }

        return new ClassificationResult(classifiedList, unclassifiedList);
    }


}
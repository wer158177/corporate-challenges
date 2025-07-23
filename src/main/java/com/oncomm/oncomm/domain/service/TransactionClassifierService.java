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
        List<MerchantKeyword> allKeywords = merchantKeywordRepository.findAllWithJoins();
        List<ClassifiedTransaction> classifiedList = new ArrayList<>();
        List<UnclassifiedTransaction> unclassifiedList = new ArrayList<>();

        for (Transaction tx : transactions) {
            Optional<MerchantKeyword> match = allKeywords.stream()
                    .filter(mk -> tx.getDescription().contains(mk.getKeyword()))
                    .findFirst();

            if (match.isPresent()) {
                MerchantKeyword mk = match.get();
                classifiedList.add(ClassifiedTransaction.from(tx, mk));
            } else {
                Company company = tx.getCompany(); // ← 이게 가능해야 함
                unclassifiedList.add(UnclassifiedTransaction.from(tx, company));
            }
        }

        return new ClassificationResult(classifiedList, unclassifiedList);
    }
}
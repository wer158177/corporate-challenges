package com.oncomm.oncomm.util.mapper;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.dto.response.ClassifiedTransactionResponse;

public class RuleMapper {

    public static ClassifiedTransactionResponse toDto(ClassifiedTransaction entity) {
        return ClassifiedTransactionResponse.builder()
                .txId(entity.getTxId())
                .companyId(entity.getCompany().getCompanyId())
                .companyName(entity.getCompanyName())
                .categoryId(entity.getCategory().getCategoryId())
                .categoryName(entity.getCategoryName())
                .matchedKeyword(entity.getMatchedKeyword())
                .classifiedAt(entity.getClassifiedAt())
                .build();
    }
}

package com.oncomm.oncomm.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClassifiedTransactionResponse {

    private final Long txId;

    private final String companyId;
    private final String companyName;

    private final String categoryId;
    private final String categoryName;

    private final String matchedKeyword;

    private final LocalDateTime classifiedAt;
}

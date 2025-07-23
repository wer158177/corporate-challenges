package com.oncomm.oncomm.dto.response;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.Transaction;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClassifiedTransactionResponse {

    private String transactionDateTime;
    private String description;
    private Long income;
    private Long expenditure;
    private String categoryId;
    private String categoryName;

    public static ClassifiedTransactionResponse fromEntity(ClassifiedTransaction tx) {
        Transaction origin = tx.getTransaction();
        return ClassifiedTransactionResponse.builder()
                .transactionDateTime(origin.getTxDatetime().toString())
                .description(origin.getDescription())
                .income(origin.getDeposit())
                .expenditure(origin.getWithdraw())
                .categoryId(tx.getCategory().getCategoryId())
                .categoryName(tx.getCategoryName())
                .build();
    }

}

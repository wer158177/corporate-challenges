package com.oncomm.oncomm.dto.response;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.Transaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Getter
@Builder
public class ClassifiedTransactionResponse {

    private String transactionDateTime;
    private String description;
    private Long income;
    private Long expenditure;
    private CategoryDTO category;

    @Value
    public static class CategoryDTO {
        String id;
        String name;
    }

    public static ClassifiedTransactionResponse fromEntity(ClassifiedTransaction tx) {
        Transaction origin = tx.getTransaction();
        return ClassifiedTransactionResponse.builder()
                .transactionDateTime(origin.getTxDatetime().toString())
                .description(origin.getDescription())
                .income(origin.getDeposit())
                .expenditure(origin.getWithdraw())
                .category(new CategoryDTO(
                        tx.getCategory().getCategoryId(),
                        tx.getCategoryName()
                ))
                .build();
    }
}
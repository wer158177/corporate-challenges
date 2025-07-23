package com.oncomm.oncomm.dto.response;

import com.oncomm.oncomm.domain.model.ClassifiedTransaction;
import com.oncomm.oncomm.domain.model.Transaction;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClassifiedTransactionResponse {

    private String occurredAt;
    private String description;
    private Long deposit;
    private Long withdraw;
    private CategoryDTO category;

    @Getter
    @Builder
    public static class CategoryDTO {
        private String id;
        private String name;
    }

    public static ClassifiedTransactionResponse from(ClassifiedTransaction tx) {
        Transaction origin = tx.getTransaction();
        return ClassifiedTransactionResponse.builder()
                .occurredAt(origin.getOccurredAt().toString())
                .description(origin.getDescription())
                .deposit(origin.getDeposit())
                .withdraw(origin.getWithdraw())
                .category(CategoryDTO.builder()
                        .id(tx.getCategory().getCategoryId())
                        .name(tx.getCategoryName())
                        .build())
                .build();
    }
}

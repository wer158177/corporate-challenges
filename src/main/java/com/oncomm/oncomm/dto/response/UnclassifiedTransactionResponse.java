package com.oncomm.oncomm.dto.response;

import com.oncomm.oncomm.domain.model.UnclassifiedTransaction;
import lombok.Builder;
import lombok.Getter;



@Getter
@Builder
public class UnclassifiedTransactionResponse {

    private String occurredAt;
    private String description;
    private Long deposit;
    private Long withdraw;

    public static UnclassifiedTransactionResponse from(UnclassifiedTransaction utx) {
        return UnclassifiedTransactionResponse.builder()
                .occurredAt(utx.getTransaction().getOccurredAt().toString()) // ✅ 필드명 정리됨
                .description(utx.getTransaction().getDescription())
                .deposit(utx.getTransaction().getDeposit())
                .withdraw(utx.getTransaction().getWithdraw())
                .build();
    }
}

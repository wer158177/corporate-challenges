package com.oncomm.oncomm.dto.response;

import lombok.Getter;



public record UnclassifiedTransactionResponse(
        String transactionDateTime,
        String description,
        Long income,
        Long expenditure
) {}
